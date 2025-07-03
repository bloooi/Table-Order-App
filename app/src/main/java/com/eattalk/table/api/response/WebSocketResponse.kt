package com.eattalk.table.api.response

import kotlinx.serialization.Serializable

@Serializable
sealed interface WebSocketResponse {
    val idempotencyKey: String
    val createdAt: String
}

//@Serializable
//sealed class DiscountPayload {
//    abstract val discount: DiscountEntry
//
//    @Serializable
//    data class Created(override val discount: DiscountEntry,) : DiscountPayload()
//
//    @Serializable
//    data class Updated(
//        override val discount: DiscountEntry,
//        val sequence: List<String>
//    ) : DiscountPayload()
//}
//
//@Serializable
//data class SequencePayload(
//    val sequence: List<String>
//)
//
//@Serializable
//sealed class CategoryPayload {
//    abstract val category: CategoryEntry
//
//    @Serializable
//    data class Created(override val category: CategoryEntry) : CategoryPayload()
//
//
//    @Serializable
//    data class Updated(
//        override val category: CategoryEntry,
//        val sequence: List<String>
//    ) : CategoryPayload()
//}
//
//@Serializable
//data class OptionGroupPayload(
//    val optionGroup: OptionGroupEntry
//)
//
//@Serializable
//data class OrderSessionPayload(
//    val orderSession: OrderSessionEntry
//)
//
//@Serializable
//data class OrderSessionIdPayload(
//    val orderSessionId: String
//)
//
//@Serializable
//data class OrderPayload(
//    val orderSessionId: String,
//    val order: OrderEntry
//)
//
//@Serializable
//data class ProductPayload(
//    val product: ProductEntry
//)
//
//@Serializable
//data class StorePayload(
//    val store: StoreEntry
//)
//
//@Serializable
//data class ZonePayload(
//    val zones : List<ZoneEntry>
//)
//
//// Category
//@Serializable
//@SerialName("category.created")
//data class CategoryCreated(
//    val payload: CategoryPayload.Created,
//    override val idempotencyKey: String,
//    override val createdAt: String
//) : WebSocketResponse
//
//@Serializable
//@SerialName("category.updated")
//data class CategoryUpdated(
//    val payload: CategoryPayload.Updated,
//    override val idempotencyKey: String,
//    override val createdAt: String
//) : WebSocketResponse
//
//@Serializable
//@SerialName("category.sequence.updated")
//data class CategorySequenceUpdated(
//    val payload: SequencePayload,
//    override val idempotencyKey: String,
//    override val createdAt: String
//) : WebSocketResponse
//
//@Serializable
//@SerialName("category.deleted")
//data class CategoryDeleted(
//    val payload: Payload,
//    override val idempotencyKey: String,
//    override val createdAt: String
//) : WebSocketResponse {
//    @Serializable
//    data class Payload(
//        val categoryId: String
//    )
//}
//
//// Discound
//@Serializable
//@SerialName("discount.created")
//data class DiscountCreated(
//    val payload: DiscountPayload.Created,
//    override val idempotencyKey: String,
//    override val createdAt: String
//) : WebSocketResponse
//
//@Serializable
//@SerialName("discount.updated")
//data class DiscountUpdated(
//    val payload: DiscountPayload.Updated,
//    override val idempotencyKey: String,
//    override val createdAt: String
//) : WebSocketResponse
//
//@Serializable
//@SerialName("discount.sequence.updated")
//data class DiscountSequenceUpdated(
//    val payload: SequencePayload,
//    override val idempotencyKey: String,
//    override val createdAt: String
//) : WebSocketResponse
//
//@Serializable
//@SerialName("discount.deleted")
//data class DiscountDeleted(
//    val payload: Payload,
//    override val idempotencyKey: String,
//    override val createdAt: String
//) : WebSocketResponse {
//    @Serializable
//    data class Payload(
//        val discountId: String
//    )
//}
//
//// Option Group
//@Serializable
//@SerialName("option_group.created")
//data class OptionGroupCreated(
//    val payload: OptionGroupPayload,
//    override val idempotencyKey: String,
//    override val createdAt: String
//) : WebSocketResponse
//
//@Serializable
//@SerialName("option_group.updated")
//data class OptionGroupUpdated(
//    val payload: OptionGroupPayload,
//    override val idempotencyKey: String,
//    override val createdAt: String
//) : WebSocketResponse
//
//@Serializable
//@SerialName("option_group.deleted")
//data class OptionGroupDeleted(
//    val payload: Payload,
//    override val idempotencyKey: String,
//    override val createdAt: String
//) : WebSocketResponse {
//    @Serializable
//    data class Payload(
//        val optionGroupId: String
//    )
//}
//
//// Order
//@Serializable
//@SerialName("order_session.created")
//data class OrderSessionCreated(
//    val payload: OrderSessionPayload,
//    override val idempotencyKey: String,
//    override val createdAt: String
//) : WebSocketResponse
//
//@Serializable
//@SerialName("order_session.updated")
//data class OrderSessionUpdated(
//    val payload: OrderSessionPayload,
//    override val idempotencyKey: String,
//    override val createdAt: String
//) : WebSocketResponse
//
//@Serializable
//@SerialName("order_session.all_order.deleted")
//data class OrderSessionAllOrderDeleted(
//    val payload: OrderSessionIdPayload,
//    override val idempotencyKey: String,
//    override val createdAt: String
//) : WebSocketResponse
//
//@Serializable
//@SerialName("order.created")
//data class OrderCreated(
//    val payload: OrderPayload,
//    override val idempotencyKey: String,
//    override val createdAt: String
//) : WebSocketResponse
//
//@Serializable
//@SerialName("order.updated")
//data class OrderUpdated(
//    val payload: Payload,
//    override val idempotencyKey: String,
//    override val createdAt: String
//) : WebSocketResponse {
//    @Serializable
//    data class OrderUpdatePayload(
//        val orderId: String,
//        val state: String,
//    )
//    @Serializable
//    data class Payload(
//        val order: OrderUpdatePayload
//    )
//}
//
//@Serializable
//@SerialName("order_detail.decremented")
//data class OrderDetailDecremented(
//    val payload: Payload,
//    override val idempotencyKey: String,
//    override val createdAt: String
//) : WebSocketResponse {
//    @Serializable
//    data class OrderDetailPayload(
//        val orderDetailId: String,
//        val currentQuantity: Int,
//    )
//    @Serializable
//    data class Payload(
//        val orderDetail: OrderDetailPayload
//    )
//}
//
//
//@Serializable
//@SerialName("order_detail.deleted")
//data class OrderDetailDeleted(
//    val payload: Payload,
//    override val idempotencyKey: String,
//    override val createdAt: String
//) : WebSocketResponse {
//    @Serializable
//    data class Payload(
//        val orderDetailId: String
//    )
//}
//
//@Serializable
//@SerialName("order.deleted")
//data class OrderDeleted(
//    val payload: Payload,
//    override val idempotencyKey: String,
//    override val createdAt: String
//) : WebSocketResponse {
//    @Serializable
//    data class Payload(
//        val orderId: String
//    )
//}
//
//
//@Serializable
//@SerialName("order_session.discounted")
//data class OrderSessionDiscount(
//    val payload: Payload,
//    override val idempotencyKey: String,
//    override val createdAt: String
//) : WebSocketResponse {
//    @Serializable
//    data class Payload(
//        val orderSessionId: String,
//        val discounts: List<String>
//    )
//}
//
//@Serializable
//@SerialName("order_detail.discounted")
//data class OrderDetailDiscount(
//    val payload: Payload,
//    override val idempotencyKey: String,
//    override val createdAt: String
//) : WebSocketResponse {
//    @Serializable
//    data class OrderSessionPayload(
//        val orderSessionId: String,
//        val details: List<OrderDetailPayload>,
//    )
//
//    @Serializable
//    data class OrderDetailPayload(
//        val orderDetailId: String,
//        val discounts: List<String>,
//    )
//    @Serializable
//    data class Payload(
//        val orderSession: OrderSessionPayload
//    )
//}
//
//@Serializable
//@SerialName("order_session.completed")
//data class OrderSessionCompleted(
//    val payload: Payload,
//    override val idempotencyKey: String,
//    override val createdAt: String
//) : WebSocketResponse {
//    @Serializable
//    data class Payload(
//        val orderSessionId: String
//    )
//}
//
//// TODO : Payment
//
//// Product
//@Serializable
//@SerialName("product.created")
//data class ProductCreated(
//    val payload: ProductPayload,
//    override val idempotencyKey: String,
//    override val createdAt: String
//) : WebSocketResponse
//
//@Serializable
//@SerialName("product.updated")
//data class ProductUpdated(
//    val payload: ProductPayload,
//    override val idempotencyKey: String,
//    override val createdAt: String
//) : WebSocketResponse
//
//@Serializable
//@SerialName("product.deleted")
//data class ProductDeleted(
//    val payload: Payload,
//    override val idempotencyKey: String,
//    override val createdAt: String
//) : WebSocketResponse {
//    @Serializable
//    data class Payload(
//        val productId: String
//    )
//}
//
//// Store
//
//// TODO : store.updated
//@Serializable
//@SerialName("store.updated")
//data class StoreUpdated(
//    val payload: StorePayload,
//    override val idempotencyKey: String,
//    override val createdAt: String
//) : WebSocketResponse
//
//@Serializable
//@SerialName("store.operation.updated")
//data class StoreOperationUpdated(
//    val payload: Payload,
//    override val idempotencyKey: String,
//    override val createdAt: String
//) : WebSocketResponse {
//    @Serializable
//    data class Payload(
//        val operation: OperationState,
//        @Serializable(with = NullableLocalDateTimeIso8601Serializer::class)
//        val openedAt: LocalDateTime?,
//        @Serializable(with = NullableLocalDateTimeIso8601Serializer::class)
//        val closedAt: LocalDateTime?,
//    )
//}
//
//@Serializable
//@SerialName("store.zones.updated")
//data class ZoneUpdated(
//    val payload: ZonePayload,
//    override val idempotencyKey: String,
//    override val createdAt: String
//) : WebSocketResponse