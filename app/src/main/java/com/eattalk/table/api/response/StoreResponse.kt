package com.eattalk.table.api.response

import com.eattalk.table.model.OperationState
import com.eattalk.table.util.NullableLocalDateTimeIso8601Serializer
import com.eattalk.table.util.Stamp
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable


/**
 * 구역(Zone) 항목 (Entry)
 */
@Serializable
data class ZoneEntry(
    val zoneId: String,
    val name: String,
    val seats: List<SeatEntry>?
)

/**
 * 매장(Store) 항목 (Entry)
 *
 * Create, List, Single, Update, Update Zones 응답 스펙에 따라
 * 일부 필드는 nullable 로 선언합니다.
 */
@Serializable
data class StoreFullEntry(
    val storeId: String,
//    val region: String? = null,             // Create에만 있음
    val name: String,
    val imageUrl: String? = null,
    val defaultLanguage: String,    // Create/Single/Update에만 있음
    val currency: String,           // Create/Single에만 있음
//    val state: String? = null,              // Create/Single에만 있음
    val supportLanguages: List<String>, // Create/Single/Update에만 있음
    val taxRate: String,            // Single에만 있음
    val optionGroups: List<OptionGroupEntry>, // Single에만 있음
    val categories: List<CategoryEntry>,   // Single에만 있음
//    val discounts: List<DiscountEntry>,    // Single에만 있음
    val products: List<ProductEntry>,     // Single에만 있음
    val zones: List<ZoneEntry>,     // Update Zones/Single에만 있음
//    val orderSessions: List<OrderSessionEntry>, // Single에만 있음
    val operation: OperationState,
    @Serializable(with = NullableLocalDateTimeIso8601Serializer::class)
    val openedAt: LocalDateTime?,
    @Serializable(with = NullableLocalDateTimeIso8601Serializer::class)
    val closedAt: LocalDateTime?,
    val stamps: List<Stamp>,
)

@Serializable
data class StoreEntry(
    val storeId: String,
//    val region: String? = null,             // Create에만 있음
    val name: String,
    val imageUrl: String? = null,
    val defaultLanguage: String,    // Create/Single/Update에만 있음
    val currency: String,           // Create/Single에만 있음
//    val state: String? = null,              // Create/Single에만 있음
    val supportLanguages: List<String>, // Create/Single/Update에만 있음
    val taxRate: String,            // Single에만 있음
)

@Serializable
data class StoreSimpleEntry(
    val storeId: String,
//    val region: String? = null,             // Create에만 있음
    val name: String,
    val imageUrl: String? = null,
)

/**
 * [Create], [Get Single], [Update] 응답 래퍼
 * {
 *   "store": { … StoreEntry … }
 * }
 */
@Serializable
data class StoreResponse(
    val store: StoreFullEntry
)

@Serializable
data class StoreUpdateResponse(
    val store: StoreEntry
)

/**
 * [Get List] 응답 래퍼
 * {
 *   "stores": [ … StoreEntry … ]
 * }
 */
@Serializable
data class StoreListResponse(
    val stores: List<StoreSimpleEntry>
)

@Serializable
data class StoreOperationResponse(
    val state: String,
)

@Serializable
data class StoreWebsocketResponse(
    val url: String,
)