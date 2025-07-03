package com.eattalk.table.room

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * Room 에서 지원하지 않는 타입들을 변환해 주는 TypeConverter 모음
 */
object Converters {

    // ─── BigDecimal ───────────────────────────────────────────────

    @TypeConverter
    @JvmStatic
    fun fromBigDecimal(value: BigDecimal?): String? =
        value?.toPlainString()

    @TypeConverter
    @JvmStatic
    fun toBigDecimal(value: String?): BigDecimal? =
        value?.let { BigDecimal(it) }


    // ─── LocalDateTime (epoch millis) ────────────────────────────

    @TypeConverter
    @JvmStatic
    fun fromLocalDateTime(value: LocalDateTime?): Long? =
        value?.toInstant(ZoneOffset.UTC)?.toEpochMilli()

    @TypeConverter
    @JvmStatic
    fun toLocalDateTime(value: Long?): LocalDateTime? =
        value?.let { Instant.ofEpochMilli(it).atOffset(ZoneOffset.UTC).toLocalDateTime() }


    // ─── UUID (String) ────────────────────────────────────────────

    @TypeConverter
    @JvmStatic
    fun fromUuid(uuid: java.util.UUID?): String? =
        uuid?.toString()

    @TypeConverter
    @JvmStatic
    fun toUuid(value: String?): java.util.UUID? =
        value?.let { java.util.UUID.fromString(it) }

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        explicitNulls = true
    }

    @TypeConverter
    fun fromMap(map: Map<String, String>): String =
        json.encodeToString(map)  // 예: {"ko":"ㅋㄴㄷㄹㅁ","en":"abcde"}

    @TypeConverter
    fun toMap(jsonStr: String): Map<String, String> =
        if (jsonStr.isBlank()) emptyMap()
        else json.decodeFromString(jsonStr)
}