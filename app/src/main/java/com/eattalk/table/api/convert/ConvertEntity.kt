package com.eattalk.table.api.convert

import com.eattalk.table.api.response.CategoryEntry
import com.eattalk.table.api.response.OptionEntry
import com.eattalk.table.api.response.OptionGroupEntry
import com.eattalk.table.api.response.OrderDetailEntry
import com.eattalk.table.api.response.OrderEntry
import com.eattalk.table.api.response.ProductEntry
import com.eattalk.table.api.response.ZoneEntry
import com.eattalk.table.room.Category
import com.eattalk.table.room.Option
import com.eattalk.table.room.OptionGroup
import com.eattalk.table.room.OptionGroupWithOptions
import com.eattalk.table.room.Order
import com.eattalk.table.room.OrderDetail
import com.eattalk.table.room.OrderDetailOption
import com.eattalk.table.room.OrderSession
import com.eattalk.table.room.Product
import com.eattalk.table.room.Seat
import com.eattalk.table.room.Zone
import com.eattalk.table.room.ZoneWithSeats
import kotlinx.datetime.Instant

// CategoryDetail -> Category 엔티티 매핑 확장함수
internal fun CategoryEntry.toEntity(
    storeId: String,
    sequenceInDisplay: Long,
): Category {
    return Category(
        categoryId = this.categoryId,
        storeId = storeId,
        translations = this.translations,
        sequenceInDisplay = sequenceInDisplay
    )
}

//// DiscountEntry → Discount 엔티티 매핑 확장함수
//internal fun DiscountEntry.toEntity(
//    storeId: String,
//    sequenceInDisplay: Long
//): Discount {
//    return Discount(
//        discountId = discountId,
//        storeId = storeId,
//        name = name,
//        type = type,
//        value = value.toDouble(),  // String → Double
//        sequenceInDisplay = sequenceInDisplay
//    )
//}

// OptionEntry → Option 엔티티 매핑 확장 함수
internal fun OptionEntry.toEntity(
    optionGroupId: String,
    sequence: Long,
): Option = Option(
    optionId = optionId,
    optionGroupId = optionGroupId,
    translations = translations,
    price = price.toDouble(),
    sequenceInDisplay = sequence,
    default = isDefault,
    maxQuantity = maxQuantity,
    outOfStock = outOfStock
)

// OptionGroupEntry → OptionGroupWithOptions 매핑 확장 함수
internal fun OptionGroupEntry.toWithOptionsEntity(
    storeId: String
): OptionGroupWithOptions {
    // 1) OptionGroup 엔티티 생성
    val groupEntity = OptionGroup(
        optionGroupId = this.optionGroupId,
        storeId = storeId,
        translations = translations,
        type = this.type,
        required = this.required,
        maxQuantity = this.maxQuantity
    )

    // 2) Option 리스트 엔티티로 변환 (순서는 리스트 인덱스 + 1)
    val optionEntities = this.options
        .orEmpty()
        .mapIndexed { index, entry ->
            entry.toEntity(
                optionGroupId = this.optionGroupId,
                sequence = index.toLong()
            )
        }

    // 3) Composite 객체 반환
    return OptionGroupWithOptions(
        optionGroup = groupEntity,
        options = optionEntities
    )
}

// OrderEntry → Order 엔티티 매핑 확장 함수
internal fun OrderEntry.toOrderEntity(
    storeId: String,
    orderSessionId: String,
): Order = Order(
    orderId = this.orderId,
    storeId = storeId,
    orderSessionId = orderSessionId,
    sequence = this.sequence,
    state = this.state,
    createdAt = Instant.parse(createdAt).toEpochMilliseconds()
)

// OrderDetailEntry → OrderDetail 엔티티 매핑 확장 함수
internal fun OrderDetailEntry.toEntity(
    orderId: String
): OrderDetail = OrderDetail(
    orderDetailId = this.orderDetailId,
    orderId = orderId,
    productId = this.productId,
    quantity = this.quantity,
    delivered = false
)

internal fun OrderDetailEntry.toOptionsEntity(): List<OrderDetailOption> =
    this.options.map { option ->
        OrderDetailOption(
            orderDetailId = this.orderDetailId,
            optionId = option.optionId,
            quantity = option.quantity,
        )
    }

// OrderEntry 전체를 Order + OrderDetail 목록으로 변환
internal fun OrderEntry.toEntities(
    storeId: String,
    orderSessionId: String,
): Triple<Order, List<OrderDetail>, List<OrderDetailOption>> {
    val orderEntity = this.toOrderEntity(storeId, orderSessionId)
    val detailEntities = this.details.map { detail ->
        detail.toEntity(orderEntity.orderId)
    }
    val orderOptionEntities: List<OrderDetailOption> = details.flatMap { detail ->
        detail.options.map { option ->
            OrderDetailOption(
                orderDetailId = detail.orderDetailId,  // detail 별 ID 사용
                optionId = option.optionId,
                quantity = option.quantity
            )
        }
    }
    return Triple(orderEntity, detailEntities, orderOptionEntities)
}

//// OrderSessionEntry → OrderSession 엔티티 매핑 확장 함수
//internal fun OrderSessionEntry.toEntity(): OrderSession = OrderSession(
//    orderSessionId = this.orderSessionId,
//    seatId = this.seatId,
//    type = this.type,
//    headCount = this.headCount ?: 0,
//    enteredAt = Instant.parse(enteredAt).toEpochMilliseconds()
//)

data class OrderEntitiesBundle(
    val orderSession: OrderSession,
    val orders: List<Order>,
    val orderDetails: List<OrderDetail>,
    val detailOptions: List<OrderDetailOption>,
)

//// OrderSessionEntry → (OrderSession + Order & OrderDetail 목록) 매핑 확장 함수
//// order 필드가 null 아니면 OrderEntry.toEntities() 사용
//internal fun OrderSessionEntry.toSessionWithOrderEntities(
//    storeId: String,
//): OrderEntitiesBundle {
//    // 1) 세션 엔티티
//    val session = this.toEntity()
//
////    // 2) order DTO가 null이면 예외 처리하거나 원하는 기본값 반환
////    val orderListEntry = this.order
////        ?: error("OrderEntry is null for session ${this.orderSessionId}")
////
////    // 3) OrderEntry → Order + OrderDetail 엔티티 변환
////    val (orderEntity, detailEntities) =
////        orderListEntry.toEntities(storeId, this.orderSessionId)
//
//    // 2) orderEntries가 null이거나 비어있으면 예외 처리하거나 빈 리스트로 처리
//    val orderEntries: List<OrderEntry> = this.orders ?: listOf()
//
//// 3) 각 OrderEntry를 toEntities()로 변환한 뒤, 엔티티 리스트로 분리
//    val pairs: List<Pair<Order, List<OrderDetail>>> = orderEntries.map { entry ->
//        val entity = entry.toEntities(
//            storeId = storeId,
//            orderSessionId = this.orderSessionId,
//        )
//        entity.first to entity.second
//    }
//
//// 4) OrderEntity만 모은 리스트
//    val orderEntities: List<Order> = pairs.map { it.first }
//
//// 5) OrderDetailEntity만 모은 리스트 (모든 리스트를 평탄화)
//    val detailEntities: List<OrderDetail> = pairs.flatMap { it.second }
//
//    val detailOptionsEntities: List<OrderDetailOption> =
//        orderEntries.flatMap { it.details }.flatMap { it.toOptionsEntity() }
//
//    return OrderEntitiesBundle(
//        orderSession = session,
//        orders = orderEntities,
//        orderDetails = detailEntities,
//        detailOptions = detailOptionsEntities
//    )
//}

// ProductEntry → Product 엔티티 매핑 확장 함수
internal fun ProductEntry.toEntity(): Product = Product(
    productId = this.productId,
    translations = this.translations
        ?: emptyMap(),
    price = this.price.toDouble(),
    backgroundColor = this.backgroundColor
        ?: "#FFFFFF",   // 기본 색상 지정
    imageUrl = this.imageUrl ?: "",
    outOfStock = this.outOfStock
)


// ZoneEntry → ZoneWithSeats 매핑 확장 함수
internal fun ZoneEntry.toWithSeatsEntity(
    storeId: String,
    sequence: Long
): ZoneWithSeats {
    // 1) Zone 엔티티 생성
    val zoneEntity = Zone(
        zoneId = this.zoneId,
        storeId = storeId,
        name = this.name,
        sequenceInDisplay = sequence,
    )

    // 2) SeatEntry 리스트 → Seat 엔티티 리스트 변환
    val seatEntities = this.seats
        .orEmpty()
        .map { seat ->
            Seat(
                seatId = seat.seatId,
                zoneId = this.zoneId,
                name = seat.name,
                x = seat.x,
                y = seat.y,
                width = seat.width,
                height = seat.height
            )
        }

    // 3) Composite 반환
    return ZoneWithSeats(
        zone = zoneEntity,
        seats = seatEntities
    )
}