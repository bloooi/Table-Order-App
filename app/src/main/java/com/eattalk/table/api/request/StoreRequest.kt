package com.eattalk.table.api.request

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data class CreateStoreRequest(
    val name: String,
    val region: String,
    val defaultLanguage: String,
    val currency: String,
    val supportLanguages: List<String>
)

@Serializable
data class UpdateStoreRequest(
    val name: String? = null,
    val imageUrl: String? = null,
    val defaultLanguage: String? = null,
    val supportLanguages: List<String>? = null
)

@Serializable
data class UpdateStoreOperationRequest(
    val state: String,
)

/**
 * 좌석 수정/생성을 위한 요청용 DTO
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class SeatRequest(
    @EncodeDefault
    val seatId: String? = null,
    val name: String,
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int
)

/**
 * 구역(zone) 수정/생성을 위한 요청용 DTO
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class ZoneRequest(
    @EncodeDefault
    val zoneId: String? = null,
    val name: String,
    val seats: List<SeatRequest>
)

/**
 * 여러 구역을 한 번에 업데이트할 때 사용하는 최상위 요청 DTO
 */
@Serializable
data class UpdateZonesRequest(
    val zones: List<ZoneRequest>
)

@Serializable
data class WebSocketRequest(
    val lastReceivedAt: String? = null,
)