package com.eattalk.table.api.websocket

// import android.content.Context // ApplicationContext는 생성자에서 제거되었으므로 필요 없어짐 (apiService가 HttpClient를 제공)
import android.util.Log
import com.eattalk.table.api.ApiService // 사용자 정의 ApiService 사용
import com.eattalk.table.api.response.WebSocketResponse
// import dagger.hilt.android.qualifiers.ApplicationContext // 제거
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.*
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.min
import kotlin.math.pow

// 인터페이스 (기존과 동일)
interface WebSocketManager {
    fun connect(urlProvider: suspend () -> String?)
    fun disconnect()
    suspend fun sendMessage(message: String)
    fun observeIncomingMessages(): SharedFlow<WebSocketResponse>
    fun observeConnectionStatus(): StateFlow<ConnectionStatus>
}

@Singleton
class KtorWebSocketManager @Inject constructor(
    private val apiService: ApiService // HttpClient를 포함한 ApiService 주입
) : WebSocketManager {

    private var session: DefaultClientWebSocketSession? = null
    private val _events = MutableSharedFlow<WebSocketResponse>(extraBufferCapacity = 64)
    private val _connectionStatus = MutableStateFlow(ConnectionStatus.DISCONNECTED)

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var connectionJob: Job? = null

    // 재연결 설정
    private val maxReconnectAttempts = 5 // 최대 재시도 횟수 (0이면 무한 시도처럼 동작, 단 아래 로직에선 0이면 시도 안함. 무한은 큰 값으로 설정)
    private val initialReconnectDelayMs = 2000L // 초기 재연결 딜레이 (2초)
    private val maxReconnectDelayMs = 30000L  // 최대 재연결 딜레이 (30초)
    private val reconnectFactor = 2.0         // 딜레이 증가 계수

    val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        isLenient = true // JSON 파싱 시 좀 더 유연하게 처리 (필요에 따라)
    }

    override fun observeIncomingMessages(): SharedFlow<WebSocketResponse> = _events.asSharedFlow()
    override fun observeConnectionStatus(): StateFlow<ConnectionStatus> = _connectionStatus.asStateFlow()

    override fun connect(urlProvider: suspend () -> String?) {
        Log.d("WebSocketManager", "connect() 호출됨.")
        // 이전 연결 작업이 있다면 취소하여 새로운 연결 시퀀스를 시작
        connectionJob?.cancel(CancellationException("새로운 connect() 호출로 인한 재시작"))

        connectionJob = coroutineScope.launch {
            var attempts = 0 // 현재 재연결 시도 횟수

            while (isActive && (maxReconnectAttempts == 0 || attempts < maxReconnectAttempts)) {
                _connectionStatus.value = ConnectionStatus.CONNECTING

                if (attempts > 0) {
                    val delayMillis = calculateReconnectDelay(attempts)
                    Log.d("WebSocketManager", "재연결 시도 ${attempts + 1} (딜레이: ${delayMillis / 1000}초)")
                    try {
                        delay(delayMillis)
                    } catch (e: CancellationException) {
                        break // 딜레이 중 취소되면 루프 종료
                    }
                }

                // ★★★ 핵심 변경: 매 시도마다 urlProvider를 호출하여 새로운 URL을 가져옴 ★★★
                val urlToConnect = try {
                    Log.d("WebSocketManager", "URL 가져오기 시도...")
                    urlProvider()
                } catch (e: Exception) {
                    Log.e("WebSocketManager", "urlProvider 실행 중 오류 발생", e)
                    null
                }

                if (urlToConnect == null) {
                    Log.w("WebSocketManager", "유효한 URL을 가져오지 못하여 연결 시도에 실패했습니다.")
                    attempts++
                    continue // 다음 재시도 루프로 넘어감
                }

                try {
                    Log.d("WebSocketManager", "$urlToConnect 연결 시도 (시도 횟수: ${attempts + 1})")
                    apiService.client.webSocket(urlString = urlToConnect) {
                        session = this
                        _connectionStatus.value = ConnectionStatus.CONNECTED
                        Log.i("WebSocketManager", "웹소켓 연결 성공: $urlToConnect")
                        attempts = 0 // 연결 성공 시 재시도 횟수 초기화

                        // 메시지 수신 루프 (기존 코드와 거의 동일)
                        try {
                            for (frame in incoming) {
                                if (!isActive) break
                                if (frame is Frame.Text) {
                                    val receivedText = frame.readText()
                                    Log.d("WebSocketManager", "메시지 수신: $receivedText\n URL: $urlToConnect")
                                    try {
                                        val event = json.decodeFromString<WebSocketResponse>(receivedText)
                                        _events.emit(event)
                                    } catch (e: Exception) {
                                        Log.e("WebSocketManager", "JSON 파싱 오류: $receivedText", e)
                                    }
                                }
                            }
                        } catch (e: ClosedReceiveChannelException) {
                            Log.w("WebSocketManager", "수신 채널 닫힘 (연결 끊김 감지): ${closeReason.await()}", e)
                        } catch (e: Throwable) {
                            Log.e("WebSocketManager", "메시지 수신 중 심각한 오류", e)
                        } finally {
                            Log.d("WebSocketManager", "메시지 수신 루프 및 내부 세션 종료. 이유: ${closeReason.await()}")
                        }
                    }

                    // webSocket { ... } 블록이 종료되면 연결은 닫힌 상태임.
                    val finalCloseReason = session?.closeReason?.await()
                    handleDisconnect(finalCloseReason)

                } catch (e: CancellationException) {
                    Log.i("WebSocketManager", "연결 작업(connectionJob)이 명시적으로 취소됨.")
                    _connectionStatus.value = ConnectionStatus.DISCONNECTED
                    session = null
                    break // while 루프 종료
                } catch (e: Exception) {
                    Log.e("WebSocketManager", "$urlToConnect 웹소켓 연결 시도 ${attempts + 1} 실패 (외부 예외)", e)
                    handleDisconnect(CloseReason(CloseReason.Codes.INTERNAL_ERROR, e.message ?: "Connection attempt failed"))
                }

                if (!isActive) break

                if (_connectionStatus.value != ConnectionStatus.CONNECTED) {
                    attempts++
                }
            } // while 루프 끝

            // 루프 종료 후 최종 상태 처리
            if (isActive && (maxReconnectAttempts > 0 && attempts >= maxReconnectAttempts)) {
                Log.w("WebSocketManager", "최대 재연결 시도 횟수($maxReconnectAttempts) 도달. 재연결 중단.")
                _connectionStatus.value = ConnectionStatus.ERROR // 또는 영구적 실패 상태
            } else if (!isActive && _connectionStatus.value != ConnectionStatus.DISCONNECTED) {
                // 작업이 외부에서 취소되었고, disconnect()에서 상태를 이미 DISCONNECTED로 설정하지 않은 경우
                _connectionStatus.value = ConnectionStatus.DISCONNECTED
            }

            // 최종적으로 연결 상태가 CONNECTED가 아니라면 세션 정리 (handleDisconnect에서 이미 처리되었을 수 있음)
            if (_connectionStatus.value != ConnectionStatus.CONNECTED) {
                session = null
            }
            Log.d("WebSocketManager", "재연결 루프 최종 종료. 현재 상태: ${_connectionStatus.value}")
        }
    }

    // 재연결 딜레이 계산 함수 (Exponential Backoff)
    private fun calculateReconnectDelay(attempts: Int): Long {
        if (attempts <= 0) return 0L // 첫 시도 또는 음수 값에 대한 방어
        val delay = initialReconnectDelayMs * (reconnectFactor.pow(attempts - 1).toLong())
        return min(delay, maxReconnectDelayMs)
    }

    // handleDisconnect는 연결이 실제로 끊어졌을 때 상태를 업데이트하고 세션을 정리하는 역할
    private fun handleDisconnect(reason: CloseReason?) {
        val currentStatus = _connectionStatus.value
        val statusToSet = ConnectionStatus.DISCONNECTED
//        val statusToSet = if (reason?.knownReason == CloseReason.Codes.NORMAL) {
//            ConnectionStatus.DISCONNECTED
//        } else {
//            ConnectionStatus.ERROR
//        }
        // 이미 명시적으로 DISCONNECTED 된 상태가 아니면서, 에러나 다른 이유로 끊긴 경우
//        if (currentStatus != ConnectionStatus.DISCONNECTED || statusToSet == ConnectionStatus.DISCONNECTED) {
        if (currentStatus != ConnectionStatus.DISCONNECTED) {
            _connectionStatus.value = statusToSet
        }
        session = null // 세션 참조 제거 (중요)
        Log.d("WebSocketManager", "handleDisconnect 호출됨. 설정된 상태: ${_connectionStatus.value}, 이유: $reason")
    }

    override fun disconnect() {
        Log.i("WebSocketManager", "disconnect() 명시적 호출됨.")
        connectionJob?.cancel(CancellationException("사용자 요청으로 명시적 연결 해제")) // 진행 중인 모든 연결/재연결 시도 취소
        connectionJob = null

        if (session?.isActive == true) { // 세션이 아직 활성 상태라면 명시적으로 닫음
            coroutineScope.launch { // close는 suspend 함수일 수 있음
                try {
                    session?.close(CloseReason(CloseReason.Codes.NORMAL, "사용자 요청으로 연결 해제"))
                } catch (e: Exception) {
                    Log.e("WebSocketManager", "세션 닫는 중 오류 발생", e)
                } finally {
                    session = null
                    _connectionStatus.value = ConnectionStatus.DISCONNECTED // 최종 상태 명시
                }
            }
        } else { // 세션이 없거나 이미 비활성 상태
            session = null
            _connectionStatus.value = ConnectionStatus.DISCONNECTED // 최종 상태 명시
        }
    }

    override suspend fun sendMessage(message: String) {
        if (_connectionStatus.value == ConnectionStatus.CONNECTED && session?.isActive == true) {
            try {
                session?.send(Frame.Text(message))
                Log.d("WebSocketManager", "메시지 전송: $message")
            } catch (e: Exception) {
                Log.e("WebSocketManager", "메시지 전송 실패", e)
                /**
                * 여기서 연결 상태를 ERROR로 변경하고 재연결을 유도할 수도 있음
                * 예: handleDisconnect(CloseReason(CloseReason.Codes.UNEXPECTED_CONDITION, "Send failed"))
                * 그러면 connect 루프가 재시도를 시작할 수 있음 (단, 현재는 connect 루프는 연결 자체에만 집중)
                * */
            }
        } else {
            Log.w("WebSocketManager", "연결되지 않은 상태(${_connectionStatus.value})에서 메시지 전송 시도: $message")
        }
    }
}

// ConnectionStatus enum (기존과 동일)
enum class ConnectionStatus {
    CONNECTING,
    CONNECTED,
    DISCONNECTED,
    ERROR
}