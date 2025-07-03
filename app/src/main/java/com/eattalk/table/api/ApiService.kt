package com.eattalk.table.api

import com.eattalk.table.BuildConfig
import com.eattalk.table.api.repository.AuthApi
import com.eattalk.table.api.repository.StoreApi
import com.eattalk.table.api.repository.remote.AuthRemoteApi
import com.eattalk.table.api.repository.remote.StoreRemoteApi
import com.eattalk.table.api.util.SessionManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.json.Json

class ApiService(
    sessionIdFlow: StateFlow<String?>,
) {
    val commonClient: HttpClient =
        HttpClient(CIO) {
            // Json serializer
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        ignoreUnknownKeys = true
                        explicitNulls = true
                    }
                )
            }
            // 2) 로깅
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.BODY
            }

            // 모든 요청에 대해 기본값 설정
            install(DefaultRequest) {
                url {
                    // 호스트·스킴·기본 경로 설정
                    protocol = URLProtocol.HTTPS
                    host = BuildConfig.API_HOST
                }
                // 공통 헤더도 함께 설정 가능
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }

            install(WebSockets) {
                // ping/pong, timeout 등 옵션 설정 가능
                pingIntervalMillis = 15_000
                maxFrameSize = Long.MAX_VALUE
                contentConverter = null
            }

            engine {
                // OkHttp 엔진 커스터마이징
                maxConnectionsCount = 1000
                endpoint {
                    // this: EndpointConfig
                    maxConnectionsPerRoute = 100
                    pipelineMaxSize = 20
                    keepAliveTime = 5000
                    connectTimeout = 5000
                    connectAttempts = 5
                }
            }

        }

    val sessionManager = SessionManager(
        sessionIdFlow = sessionIdFlow,
        refreshClient = commonClient
    )

    val client = commonClient.apply {
        plugin(HttpSend).intercept { request ->
            // 1) 요청 직전에 최신 토큰 설정
            sessionIdFlow.value?.let {
                if (it.isEmpty()) return@let
                request.headers[HttpHeaders.Authorization] = "Bearer $it"
            }

            // 2) 실제 요청 실행
            val call = execute(request)

            // 3) 401 Unauthorized 시 연장 + 재시도
            if (call.response.status == HttpStatusCode.Unauthorized) {
                val newToken = sessionManager.refreshSessionIfNeeded()
                request.headers[HttpHeaders.Authorization] = "Bearer $newToken"
                return@intercept execute(request)
            }
            call
        }
    }

    fun authApi(): AuthApi = AuthRemoteApi(commonClient, client)
    fun storeApi(): StoreApi = StoreRemoteApi(client)

    companion object {
        val apiPath = "/api/pos"
    }
}