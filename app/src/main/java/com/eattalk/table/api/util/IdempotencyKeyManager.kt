package com.eattalk.table.api.util

import com.eattalk.table.util.UUIDv7
import java.util.concurrent.ConcurrentHashMap

object IdempotencyKeyManager {
    // endpoint(URL or key) -> current key
    private val keys = ConcurrentHashMap<String, String>()

    /**
     * 해당 엔드포인트에 대한 현재 키를 반환.
     * 없으면 새로 생성해 저장합니다.
     */
    fun getKeyFor(endpoint: String): String =
        keys.computeIfAbsent(endpoint) {
            UUIDv7.generateString()
        }

    /**
     * 성공 응답을 받으면 해당 엔드포인트의 키를 새로 생성합니다.
     */
    fun generateNewKeyFor(endpoint: String) {
        keys[endpoint] = UUIDv7.generateString()
    }

    /** 실패 시에는 아무 동작도 하지 않아, 동일 키가 유지됩니다. */
    fun keepKeyOnFailureFor(endpoint: String) { /* no-op */ }
}