package com.eattalk.table.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 옵션 엔트리 (Option)
 * - Create/Update 응답에는 optionGroupId, translationId, createdAt 필드가 포함됩니다.
 * - Get(single) 응답에는 이들 중 일부가 생략될 수 있으므로 모두 nullable 로 선언합니다.
 */
@Serializable
data class OptionEntry(
    val optionId: String,
    @SerialName("optionGroupId")
    val optionGroupId: String? = null,
    @SerialName("translationId")
    val translationId: String? = null,
    val price: String,
    @SerialName("default")
    val isDefault: Boolean,
    val maxQuantity: Int,
    val outOfStock: Boolean,
    val createdAt: String? = null,
    val translations: Map<String, String>,
)

/**
 * 옵션 그룹 엔트리 (OptionGroup)
 * - List 응답에는 options/translationId/createdAt 필드가 없으므로 nullable 로 선언합니다.
 */
@Serializable
data class OptionGroupEntry(
    val optionGroupId: String,
    @SerialName("translationId")
    val translationId: String? = null,
    val type: String,
    val required: Boolean,
    val maxQuantity: Int,
    val createdAt: String? = null,
    val translations: Map<String, String>,
    val options: List<OptionEntry>? = null
)

// 카테고리 풀 정보 (Create 시 storeId·translationId 포함)
@Serializable
data class CategoryEntry(
    val categoryId: String,
    val translationId: String? = null,
    val translations: Map<String, String>,
    val products: List<String>? = null
)


/**
 * 상품 엔트리 (ProductEntry)
 *
 * Create, List, Single, Update 응답 스펙에 따라
 * 일부 필드는 nullable 로 선언합니다.
 */
@Serializable
data class ProductEntry(
    val productId: String,
    val price: String,
    val categoryId: String? = null,           // Create에만 있음
    val optionGroups: List<String>? = null,   // Create에만 있음
    val imageUrl: String? = null,
    val outOfStock: Boolean,
    val backgroundColor: String? = null,
    val tags: List<String>? = null,           // Create/Update에만 있음
    val translationId: String? = null,        // List에만 있음
    val translations: Map<String, String>? = null  // Create/Single에만 있음
)

/**
 * 주문 상세 옵션 수량 엔트리
 */
@Serializable
data class OrderOptionQuantityEntry(
    val optionId: String,
    val quantity: Int
)

@Serializable
data class ManualDiscount(
    val discountId: String,
    val type: String,
    val value: String
)

/**
 * 주문 상세 항목 엔트리
 */
@Serializable
data class OrderDetailEntry(
    val orderDetailId: String,
    val productId: String,
    val quantity: Int,
    val options: List<OrderOptionQuantityEntry>,
    val discounts: List<String> = emptyList(),
    val manualDiscount: ManualDiscount? = null,
)

/**
 * 주문 엔트리 (TAKE_OUT일 때만 포함)
 */
@Serializable
data class OrderEntry(
    val orderId: String,
    val orderSessionId: String? = null,
    val state: String,
    val sequence: Int,
    val createdAt: String,
    val details: List<OrderDetailEntry>
)

@Serializable
data class SeatEntry(
    val seatId: String,
    val name: String,
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int
)
