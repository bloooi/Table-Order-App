package com.eattalk.table.api.util

import com.eattalk.table.api.ApiService
import io.ktor.client.HttpClient
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject

class SessionManager @Inject constructor(
    val sessionIdFlow: StateFlow<String?>,
    private val refreshClient: HttpClient // 연장용 엔드포인트만 호출하는 클라이언트
) {
    companion object {
        // 1분 쿨다운
        private val COOLDOWN_MS = TimeUnit.MINUTES.toMillis(1)
    }

    private val mutex = Mutex()
    private val lastRefresh = AtomicLong(0)

    /**
     * 401 발생 시 호출: 동시성·쿨다운 제어 후 연장 API 호출
     * SessionId 는 변하지 않으므로 그대로 사용
     */
    suspend fun refreshSessionIfNeeded(): String {
        val sessionId = sessionIdFlow.value ?: ""
        val now = System.currentTimeMillis()
        // 쿨다운 체크
        if (now - lastRefresh.get() < COOLDOWN_MS) {
            return sessionId
        }
        return mutex.withLock {
            // 이중 체크
            val now2 = System.currentTimeMillis()
            if (now2 - lastRefresh.get() < COOLDOWN_MS) {
                return@withLock sessionId
            }
            // Flow 기반 연장 API 호출
            val state = refreshClient
                .requestFlow<HttpResponse>(
                    method = HttpMethod.Post,
                    urlString = "${ApiService.apiPath}/auth/refresh"
                )
                .first()

            when (state) {
                is State.Success -> {
                    lastRefresh.set(System.currentTimeMillis())
                    sessionId
                }

                is State.Error -> sessionId
            }
        }
    }
}