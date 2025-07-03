package com.eattalk.table.util

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

// Nullable LocalDateTime을 위한 시리얼라이저
@OptIn(ExperimentalSerializationApi::class)
object NullableLocalDateTimeIso8601Serializer : KSerializer<LocalDateTime?> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("NullableLocalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDateTime?) {
        if (value == null) {
            encoder.encodeNull()
        } else {
            // LocalDateTime을 UTC Instant로 변환 후 ISO 8601 문자열로 직렬화
            // 실제 저장 시점의 시간대 정보가 중요하다면, 해당 시간대를 명시해야 합니다.
            // 여기서는 시스템 기본 시간대를 기준으로 UTC로 변환합니다.
            encoder.encodeString(value.toInstant(TimeZone.currentSystemDefault()).toString())
        }
    }

    override fun deserialize(decoder: Decoder): LocalDateTime? {
        if (decoder.decodeNotNullMark()) { // null이 아닌 경우
            val string = decoder.decodeString()
            return try {
                // ISO 8601 문자열을 Instant로 파싱 후 시스템 기본 시간대의 LocalDateTime으로 변환
                Instant.parse(string).toLocalDateTime(TimeZone.currentSystemDefault())
            } catch (e: Exception) {
                // 파싱 실패 시 예외 처리 (예: null 반환 또는 에러 로깅)
                // 요구사항에 따라 다른 기본값을 반환하거나 예외를 다시 던질 수 있습니다.
                null
            }
        } else { // null인 경우
            return decoder.decodeNull()
        }
    }
}