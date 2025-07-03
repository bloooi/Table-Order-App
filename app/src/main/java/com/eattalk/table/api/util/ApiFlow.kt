package com.eattalk.table.api.util

import android.util.Log
import com.eattalk.table.api.response.EatTalkException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.HttpMethod
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


inline fun <reified T : Any> HttpClient.requestFlow(
    method: HttpMethod,
    urlString: String,
    noinline block: HttpRequestBuilder.() -> Unit = {}
): Flow<State<T>> = flow {
    // 1) 엔드포인트 별 현재 키 가져오기₩
    val key = if (method in listOf(HttpMethod.Post, HttpMethod.Put, HttpMethod.Patch, HttpMethod.Delete)){
        IdempotencyKeyManager.getKeyFor(urlString)
    }else {
        null
    }

    try {
        // 2) 요청 실행 (헤더 + 기존 block 적용)
        val response: HttpResponse = this@requestFlow.request {
            url(urlString)
            this.method = method

            key?.apply {
                header("Idempotency-Key", key)
            }
            block()
        }

        // 3) 성공 여부에 따라 키 갱신 또는 유지
        if (response.status.isSuccess()) {
            IdempotencyKeyManager.generateNewKeyFor(urlString)
            // 4) 바디 파싱 후 성공 State 발행
            val body: T = response.body()
            val date = response.headers["Date"]
            response.bodyAsText()
            Log.i("requestFlow", "url : ${response.request.url}\nbody: ${response.bodyAsText()}")
            emit(State.success(body, key, date))
        } else {
            IdempotencyKeyManager.keepKeyOnFailureFor(urlString)
            val exception = EatTalkException.create(response)
            emit(State.fail(exception))
            Log.e("requestFlow", "url : ${response.request.url}\ncode : ${response.status}\nbody: ${response.bodyAsText()}")
            response.bodyAsText()
            response.request
        }
    } catch (e: Exception) {
        // 5) Request 에러 등 예외 시에도 키 유지
        IdempotencyKeyManager.keepKeyOnFailureFor(urlString)
        Log.e("requestFlow", e.toString())
        emit(State.fail(e))
    }
}.flowOn(Dispatchers.IO)
