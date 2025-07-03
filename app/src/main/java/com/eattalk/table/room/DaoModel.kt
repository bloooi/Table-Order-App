package com.eattalk.table.room

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/**
 * 카테고리와 각 카테고리에 속한 ProductFull 리스트를
 * 한 번에 내려주는 데이터 클래스
 */
data class CategoryWithProductFulls(
    @Embedded val category: Category,

    @Relation(
        entity = Product::class,
        parentColumn = "category_id",
        entityColumn = "product_id",
        associateBy = Junction(
            value = CategoryProduct::class,
            parentColumn = "category_id",
            entityColumn = "product_id"
        )
    )
    val products: List<ProductFull>
)

/**
 * Represents an option group and its associated options.
 */
data class OptionGroupWithOptions(
    @Embedded val optionGroup: OptionGroup,
    @Relation(
        entity = Option::class,
        parentColumn = "option_group_id",
        entityColumn = "option_group_id"
    )
    val options: List<Option>
)

// 3) 상품 풀 스펙
data class ProductFull(
    @Embedded val product: Product,

// ── 단일 카테고리 ──
    @Relation(
        entity = Category::class,
        parentColumn = "product_id",      // Product.productId
        entityColumn = "category_id",     // CategoryProduct.categoryId
        associateBy = Junction(
            value = CategoryProduct::class,
            parentColumn = "product_id",
            entityColumn = "category_id"
        )
    )
    val category: Category?,               // 기존 List<Category> → Category?


    // ── 태그 (1:N) ──
    @Relation(
        parentColumn = "product_id",
        entityColumn = "product_id"
    )
    val tags: List<ProductTag>,

    // ── 옵션그룹 ＋ 옵션 (다대다 + 중첩) ──
    @Relation(
        entity = OptionGroup::class,
        parentColumn = "product_id",
        entityColumn = "option_group_id",
        associateBy = Junction(
            value = ProductOptionGroup::class,
            parentColumn = "product_id",
            entityColumn = "option_group_id"
        )
    )
    val optionGroups: List<OptionGroupWithOptions>,
)

/**
 * Represents a zone (area) and its seats (tables).
 */
data class ZoneWithSeats(
    @Embedded val zone: Zone,
    @Relation(
        parentColumn = "zone_id",
        entityColumn = "zone_id"
    )
    val seats: List<Seat>
)


// 2) 세션 + 세부 주문 목록
data class SessionFull(
    @Embedded val session: OrderSession,

    @Relation(
        parentColumn = "order_session_id",
        entityColumn = "order_session_id",
        entity = Order::class
    )
    val orders: List<OrderWithDetails>
)

// 3) 좌석(테이블) + “진행 중 세션(있으면)” + 세션 내 주문+상세
data class SeatWithActiveSession(
    @Embedded val seat: Seat,

    @Relation(
        parentColumn = "seat_id",
        entityColumn = "seat_id",
        entity = OrderSession::class,
        projection = [ /* 별도 projection 없이 전체 SessionFull 객체를 내려줄 것이므로 생략 */]
    )
    val sessionFull: SessionFull?
)

// 4) 구역(Zone) + 그 아래 좌석들(SeatWithActiveSession)
data class ZoneWithSeatsAndSessions(
    @Embedded val zone: Zone,

    @Relation(
        entity = Seat::class,
        parentColumn = "zone_id",
        entityColumn = "zone_id"
    )
    val seats: List<SeatWithActiveSession>
)

// 2) 주문 헤더 + (1)의 리스트
data class OrderWithDetailsAndSessionWithSeats(
    @Embedded val order: Order,
    @Relation(
        entity = OrderDetail::class,
        parentColumn = "order_id",
        entityColumn = "order_id"
    )
    val details: List<OrderDetailWithProductAndOptions>,

    @Relation(
        parentColumn = "order_session_id",
        entityColumn = "order_session_id",
        entity = OrderSession::class
    )
    val orderSessionInfo: OrderSessionWithSeat?,
)

// 2) 주문 헤더 + (1)의 리스트
data class OrderWithDetails(
    @Embedded val order: Order,
    @Relation(
        entity = OrderDetail::class,
        parentColumn = "order_id",
        entityColumn = "order_id"
    )
    val details: List<OrderDetailWithProductAndOptions>,
)


// 4) 좌석(Seat) + (3)의 리스트
data class SeatWithSessions(
    @Embedded val seat: Seat,
    @Relation(
        parentColumn = "seat_id",
        entityColumn = "seat_id",
        entity = OrderSession::class
    )
    val sessions: List<SessionFull>
)

// 중간 다리용
data class OrderSessionWithSeat(
    @Embedded
    val orderSession: OrderSession,
    @Relation(
        parentColumn = "seat_id",
        entityColumn = "seat_id",
        entity = Seat::class
    )
    val seat: Seat?
)

// 2) OrderDetailWith… 에 relation 추가
data class OrderDetailWithProductAndOptions(
    @Embedded val detail: OrderDetail,

    @Relation(
        parentColumn = "product_id",
        entityColumn = "product_id"
    )
    val product: Product,

    @Relation(
        entity = OptionWithQuantityView::class,
        parentColumn = "order_detail_id",
        entityColumn = "order_detail_id"
    )
    val options: List<OptionWithQuantityView>
)


data class DiscountWithDetailId(
    @ColumnInfo(name = "order_detail_id")
    val orderDetailId: String,

    @Embedded
    val discount: Discount
)

