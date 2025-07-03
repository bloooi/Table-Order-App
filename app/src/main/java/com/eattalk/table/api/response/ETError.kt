package com.eattalk.table.api.response

import com.eattalk.table.util.UUIDv7
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

/**
 * API 에서 반환하는 공통 에러 응답 포맷
 */
@Serializable
data class EatTalkError(
    val code: String,
    val errors: List<ValidationError>
)

/**
 * 개별 검증 에러 항목
 */
@Serializable
data class ValidationError(
    val kind: String,
    val type: String,
    val input: JsonElement,
    val expected: JsonElement,
    val received: JsonElement,
    val message: String,
    val requirement: JsonElement,
    val path: List<PathItem>
)

/**
 * 에러 경로 정보 (어떤 필드에서 에러가 발생했는지)
 */
@Serializable
data class PathItem(
    val type: String,
    val origin: String,
    val input: JsonElement,
    val key: String,
    val value: JsonElement
)

/**
 * 공통 API 에러를 래핑하는 예외 클래스
 */
class EatTalkException(val uuid: String) : RuntimeException() {
    var errorResponse: EatTalkError? = null
        private set
    override var message: String? = null

    companion object {
        const val DEFAULT_MESSAGE = "일시적인 오류가 발생하였습니다."

        // kotlinx.serialization Json 인스턴스
        private val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues  = true
        }

        /**
         * Ktor ResponseException 으로부터 EatTalkError 를 파싱해 EatTalkException 으로 변환합니다.
         * 반드시 suspend 컨텍스트에서 호출하세요.
         */
        suspend fun create(response: HttpResponse): EatTalkException {
            val ex = EatTalkException(UUIDv7.generateString())
            try {
                // HTTP 응답 바디를 텍스트로 읽어서 JSON 파싱
                val text = response.bodyAsText()
                val error = json.decodeFromString<EatTalkError>(text)
                ex.errorResponse = error
                ex.message = error.errors.joinToString("\n") { it.message }
            } catch (cause: Exception) {
                // 파싱 실패 시 기본 메시지로 기록
//                CrashlyticsUtil.errorLog(cause, DEFAULT_MESSAGE)
                ex.message = DEFAULT_MESSAGE
            }
            return ex
        }

        /**
         * 이미 파싱된 EatTalkError 로부터 바로 예외를 생성합니다.
         */
        fun create(error: EatTalkError): EatTalkException {
            val ex = EatTalkException(UUIDv7.generateString())
            ex.errorResponse = error
            ex.message = error.errors.joinToString("\n") { it.message }
            return ex
        }
    }
}
