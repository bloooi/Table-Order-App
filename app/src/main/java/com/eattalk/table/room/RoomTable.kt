package com.eattalk.table.room

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable


@Entity(
    tableName = "zone",
//    foreignKeys = [
//        ForeignKey(
//            entity = Store::class,
//            parentColumns = ["store_id"],
//            childColumns = ["store_id"]
//        )
//    ],
    indices = [
//        Index("tenant_id"),
//        Index("store_id")
    ]
)
// 구역 (Area)
data class Zone(
    @PrimaryKey
    @ColumnInfo(name = "zone_id")
    val zoneId: String,

//    @ColumnInfo(name = "tenant_id")
//    val tenantId: String,

    @ColumnInfo(name = "store_id")
    val storeId: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "sequence_in_display")
    val sequenceInDisplay: Long,
//
//    @ColumnInfo(name = "created_at")
//    val createdAt: Long,
//
//    @ColumnInfo(name = "updated_at")
//    val updatedAt: Long,
//
//    @ColumnInfo(name = "deleted_at")
//    val deletedAt: Long?
)

@Entity(
    tableName = "seat",
    foreignKeys = [
//        ForeignKey(
//            entity = Store::class,
//            parentColumns = ["store_id"],
//            childColumns = ["store_id"]
//        ),
        ForeignKey(
            entity = Zone::class,
            parentColumns = ["zone_id"],
            childColumns = ["zone_id"]
        )
    ],
    indices = [
//        Index("tenant_id"),
//        Index("store_id"),
        Index("zone_id")
    ]
)
// 테이블
data class Seat(
    @PrimaryKey
    @ColumnInfo(name = "seat_id")
    val seatId: String,

//    @ColumnInfo(name = "tenant_id")
//    val tenantId: String,

//    @ColumnInfo(name = "store_id")
//    val storeId: String,

    @ColumnInfo(name = "zone_id")
    val zoneId: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "x")
    val x: Int,

    @ColumnInfo(name = "y")
    val y: Int,

    @ColumnInfo(name = "width")
    val width: Int,

    @ColumnInfo(name = "height")
    val height: Int,

//    @ColumnInfo(name = "created_at")
//    val createdAt: Long,
//
//    @ColumnInfo(name = "updated_at")
//    val updatedAt: Long,
//
//    @ColumnInfo(name = "deleted_at")
//    val deletedAt: Long?
)


@Entity(
    tableName = "order_session",
    foreignKeys = [
//        ForeignKey(
//            entity = Store::class,
//            parentColumns = ["store_id"],
//            childColumns = ["store_id"]
//        ),
        ForeignKey(
            entity = Seat::class,
            parentColumns = ["seat_id"],
            childColumns = ["seat_id"]
        ),
    ],
    indices = [
//        Index("tenant_id"),
//        Index("store_id"),
        Index("seat_id"),
//        Index("customer_id")
    ]
)
/*
* 주문 세션
* 자리 앉을 때부터 계산 까지의 세션
* order_session : order = 1:N
* */

data class OrderSession(
    @PrimaryKey
    @ColumnInfo(name = "order_session_id")
    val orderSessionId: String,

//    @ColumnInfo(name = "tenant_id")
//    val tenantId: String,

//    @ColumnInfo(name = "store_id")
//    val storeId: String,

    @ColumnInfo(name = "seat_id")
    val seatId: String?, // SQL 기준 Nullable
// 해당 주문세션을 결제할 때에 어떤 고객이 결제한 건지 기록하기 위함.
//    @ColumnInfo(name = "customer_id")
//    val customerId: String?, // SQL 기준 Nullable

    @ColumnInfo(name = "type")
    val type: String, // 예: 'DINE_IN', 'TAKE_OUT'

    @ColumnInfo(name = "head_count")
    val headCount: Int,

//    @ColumnInfo(name = "updated_at")
//    val updatedAt: Long,
//
    @ColumnInfo(name = "entered_at")
    val enteredAt: Long,
//
//    @ColumnInfo(name = "exited_at")
//    val exitedAt: Long?
)

@Entity(
    tableName = "order",
    foreignKeys = [
//        ForeignKey(
//            entity = Store::class,
//            parentColumns = ["store_id"],
//            childColumns = ["store_id"]
//        ),
        ForeignKey(
            entity = OrderSession::class,
            parentColumns = ["order_session_id"],
            childColumns = ["order_session_id"]
        )
    ],
    indices = [
//        Index("tenant_id"),
//        Index("store_id"),
        Index("order_session_id")
    ]
)

/*
* 주문 정보
* 세션 내의 주문
* order_session : order = 1:N
* */
data class Order(
    @PrimaryKey
    @ColumnInfo(name = "order_id")
    val orderId: String,

//    @ColumnInfo(name = "tenant_id")
//    val tenantId: String,

    @ColumnInfo(name = "store_id")
    val storeId: String,

    @ColumnInfo(name = "order_session_id")
    val orderSessionId: String,

    @ColumnInfo(name = "sequence")
    val sequence: Int,

    @ColumnInfo(name = "state")
    val state: String, // 예: 'RECEIVED', 'CONFIRMED', 'DELIVERED', 'CANCELLED'

    @ColumnInfo(name = "created_at")
    val createdAt: Long,
//
//    @ColumnInfo(name = "updated_at")
//    val updatedAt: Long,
//
//    @ColumnInfo(name = "deleted_at")
//    val deletedAt: Long?
)

@Entity(
    tableName = "product", // OrderDetail 전에 Product 정의
    foreignKeys = [
//        ForeignKey(
//            entity = Store::class,
//            parentColumns = ["store_id"],
//            childColumns = ["store_id"]
//        ),
    ],
    indices = [
//        Index("tenant_id"),
//        Index("store_id"),
//        Index("resource_id")
    ]

)
// 상품(메뉴)
@Serializable
data class Product(
    @PrimaryKey
    @ColumnInfo(name = "product_id")
    val productId: String,

//    @ColumnInfo(name = "tenant_id")
//    val tenantId: String,

//    @ColumnInfo(name = "store_id")
//    val storeId: String,

    @ColumnInfo(name = "translations")
    val translations: Map<String, String> = emptyMap(),

//    @ColumnInfo(name = "resource_id")
//    val resourceId: String,

    @ColumnInfo(name = "image_url")
    val imageUrl: String?,

    @ColumnInfo(name = "price")
    val price: Double, // NUMERIC을 Double로 매핑

    @ColumnInfo(name = "background_color")
    val backgroundColor: String, // CHAR(7)

    @ColumnInfo(name = "out_of_stock")
    val outOfStock: Boolean,

//    @ColumnInfo(name = "created_at")
//    val createdAt: Long,
//
//    @ColumnInfo(name = "updated_at")
//    val updatedAt: Long,
//
//    @ColumnInfo(name = "deleted_at")
//    val deletedAt: Long?
)


@Entity(
    tableName = "order_detail",
    foreignKeys = [
        ForeignKey(
            entity = Order::class,
            parentColumns = ["order_id"],
            childColumns = ["order_id"]
        ),
        ForeignKey(
            entity = Product::class,
            parentColumns = ["product_id"],
            childColumns = ["product_id"]
        )
    ],
    indices = [
//        Index("tenant_id"),
        Index("order_id"),
        Index("product_id")
    ]
)
// 주문한 상품(메뉴)
data class OrderDetail(
    @PrimaryKey
    @ColumnInfo(name = "order_detail_id")
    val orderDetailId: String,

//    @ColumnInfo(name = "tenant_id")
//    val tenantId: String,

    @ColumnInfo(name = "order_id")
    val orderId: String,

    @ColumnInfo(name = "product_id")
    val productId: String,

    @ColumnInfo(name = "quantity")
    val quantity: Int,

    @ColumnInfo(name = "delivered")
    val delivered: Boolean,

//    @ColumnInfo(name = "created_at")
//    val createdAt: Long,
//
//    @ColumnInfo(name = "updated_at")
//    val updatedAt: Long,
//
//    @ColumnInfo(name = "deleted_at")
//    val deletedAt: Long?
)


@Entity(
    tableName = "option_group", // Option 전에 정의
    foreignKeys = [
//        ForeignKey(
//            entity = Store::class,
//            parentColumns = ["store_id"],
//            childColumns = ["store_id"]
//        ),
    ],
    indices = [
//        Index("tenant_id"),
//        Index("store_id")
    ]
)
// 옵션 그룹
@Serializable
data class OptionGroup(
    @PrimaryKey
    @ColumnInfo(name = "option_group_id")
    val optionGroupId: String,

//    @ColumnInfo(name = "tenant_id")
//    val tenantId: String,

    @ColumnInfo(name = "store_id")
    val storeId: String,

    @ColumnInfo(name = "translations")
    val translations: Map<String, String> = emptyMap(),

    @ColumnInfo(name = "type")
    val type: String, // 예: 'SELECTOR', 'AMOUNT'

    @ColumnInfo(name = "required")
    val required: Boolean,

    @ColumnInfo(name = "max_quantity")
    val maxQuantity: Int,

//    @ColumnInfo(name = "created_at")
//    val createdAt: Long,
//
//    @ColumnInfo(name = "updated_at")
//    val updatedAt: Long,
//
//    @ColumnInfo(name = "deleted_at")
//    val deletedAt: Long?
)


@Entity(
    tableName = "option", // OrderDetailOption 전에 정의
    foreignKeys = [
        ForeignKey(
            entity = OptionGroup::class,
            parentColumns = ["option_group_id"],
            childColumns = ["option_group_id"]
        ),
    ],
    indices = [
//        Index("tenant_id"),
        Index("option_group_id")
    ]
)
// 옵션 (옵션 그룹 내 한개의 옵션)
@Serializable
data class Option(
    @PrimaryKey
    @ColumnInfo(name = "option_id")
    val optionId: String,

//    @ColumnInfo(name = "tenant_id")
//    val tenantId: String,

    @ColumnInfo(name = "option_group_id")
    val optionGroupId: String,

    @ColumnInfo(name = "translations")
    val translations: Map<String, String> = emptyMap(),

    @ColumnInfo(name = "price")
    val price: Double, // NUMERIC을 Double로 매핑

    @ColumnInfo(name = "sequence_in_display")
    val sequenceInDisplay: Long,

    @ColumnInfo(name = "default") // 'default'는 Kotlin 키워드이므로 백틱으로 감싸기
    val default: Boolean,

    @ColumnInfo(name = "max_quantity")
    val maxQuantity: Int,

    @ColumnInfo(name = "out_of_stock")
    val outOfStock: Boolean,

//    @ColumnInfo(name = "created_at")
//    val createdAt: Long,
//
//    @ColumnInfo(name = "updated_at")
//    val updatedAt: Long,
//
//    @ColumnInfo(name = "deleted_at")
//    val deletedAt: Long?
)


@Entity(
    tableName = "order_detail_option",
    // 매핑 테이블처럼 보이지만 SQL에서 자체 PK(order_detail_id)를 가짐.
    // 여기서 order_detail_id는 order_detail에 대한 외래 키여야 하고
    // 실제 기본 키는 복합(order_detail_id, option_id)이거나 고유 ID여야 한다고 가정.
    // 직접 변환을 위해 SQL 정의를 따름:
    primaryKeys = ["order_detail_id", "option_id"], // 조정됨: 복합 PK가 의도된 것으로 가정
    foreignKeys = [
        ForeignKey(
            entity = OrderDetail::class,
            parentColumns = ["order_detail_id"],
            childColumns = ["order_detail_id"]
        ),
        ForeignKey(
            entity = Option::class,
            parentColumns = ["option_id"],
            childColumns = ["option_id"]
        )
    ],
    indices = [
        Index("order_detail_id"),
        Index("option_id")
    ] // 가정한 복합 PK 기반 인덱스
)
// 주문한 상품(메뉴)에 들어간 옵션
data class OrderDetailOption(
    // @PrimaryKey // 복합 PK이므로 제거
    @ColumnInfo(name = "order_detail_id")
    val orderDetailId: String,

    @ColumnInfo(name = "option_id")
    val optionId: String,

    @ColumnInfo(name = "quantity")
    val quantity: Int,

//    @ColumnInfo(name = "created_at")
//    val createdAt: Long,
//
//    @ColumnInfo(name = "updated_at")
//    val updatedAt: Long,
//
//    @ColumnInfo(name = "deleted_at")
//    val deletedAt: Long?
)

@DatabaseView("""
  SELECT 
    od.order_detail_id   AS order_detail_id,
    o.option_id          AS option_id,
    o.option_group_id    AS option_group_id,
    o.translations       AS translations,
    o.price              AS price,
    o.sequence_in_display AS sequence_in_display,
    o.`default`          AS `default`,
    o.max_quantity       AS max_quantity,
    o.out_of_stock       AS out_of_stock,
    od.quantity          AS quantity
  FROM `option` AS o
  JOIN order_detail_option AS od
    ON o.option_id = od.option_id
""")
data class OptionWithQuantityView(
    @Embedded val option: Option,
    val quantity: Int,
    @ColumnInfo(name = "order_detail_id") val orderDetailId: String
)

@Entity(
    tableName = "product_option_group",
    primaryKeys = ["product_id", "option_group_id"],
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = ["product_id"],
            childColumns = ["product_id"]
        ),
        ForeignKey(
            entity = OptionGroup::class,
            parentColumns = ["option_group_id"],
            childColumns = ["option_group_id"]
        )
    ],
    indices = [
        Index("product_id"),
        Index("option_group_id")
    ]
)
// 상품에 해당하는 옵션 그룹
data class ProductOptionGroup(
    @ColumnInfo(name = "product_id")
    val productId: String,

    @ColumnInfo(name = "option_group_id")
    val optionGroupId: String,

//    @ColumnInfo(name = "created_at")
//    val createdAt: Long
)

@Entity(
    tableName = "product_tag",
    primaryKeys = ["product_id", "tag"],
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = ["product_id"],
            childColumns = ["product_id"]
        )
    ],
    indices = [Index("product_id"), Index("tag")] // 조회를 위한 태그 인덱스
)
// 상품 내 태그
data class ProductTag(
    @ColumnInfo(name = "product_id")
    val productId: String,

    @ColumnInfo(name = "tag")
    val tag: String,
//
//    @ColumnInfo(name = "created_at")
//    val createdAt: Long
)

@Entity(
    tableName = "category",
    foreignKeys = [
//        ForeignKey(
//            entity = Store::class,
//            parentColumns = ["store_id"],
//            childColumns = ["store_id"]
//        ),
    ],
    indices = [
//        Index("tenant_id"),
        Index("store_id")
    ]
)
// 카테고리
@Serializable
data class Category(
    @PrimaryKey
    @ColumnInfo(name = "category_id")
    val categoryId: String,

//    @ColumnInfo(name = "tenant_id")
//    val tenantId: String,

    @ColumnInfo(name = "store_id")
    val storeId: String,

    @ColumnInfo(name = "translations")
    val translations: Map<String, String> = emptyMap(),

    @ColumnInfo(name = "sequence_in_display")
    val sequenceInDisplay: Long,

//    @ColumnInfo(name = "created_at")
//    val createdAt: Long,
//
//    @ColumnInfo(name = "updated_at")
//    val updatedAt: Long,
//
//    @ColumnInfo(name = "deleted_at")
//    val deletedAt: Long?
)

@Entity(
    tableName = "category_product",
    primaryKeys = ["category_id", "product_id"],
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["category_id"],
            childColumns = ["category_id"]
        ),
        ForeignKey(
            entity = Product::class,
            parentColumns = ["product_id"],
            childColumns = ["product_id"]
        )
    ],
    indices = [Index("category_id"), Index("product_id")]
)
//.상품과 카테고리 관계
data class CategoryProduct(
    @ColumnInfo(name = "category_id")
    val categoryId: String,

    @ColumnInfo(name = "product_id")
    val productId: String,

    @ColumnInfo(name = "sequence_in_display")
    val sequenceInDisplay: Long,

//    @ColumnInfo(name = "created_at")
//    val createdAt: Long,
//
//    @ColumnInfo(name = "updated_at")
//    val updatedAt: Long // SQL 기준 non-null로 가정
)

@Entity(
    tableName = "discount",
    foreignKeys = [
//        ForeignKey(
//            entity = Store::class,
//            parentColumns = ["store_id"],
//            childColumns = ["store_id"]
//        )
    ],
    indices = [
//        Index("tenant_id"),
        Index("store_id")
    ]
)
/*
* 할인 정보
* 미리 만들어 둔 할인 태그 개념
* */
data class Discount(
    @PrimaryKey
    @ColumnInfo(name = "discount_id")
    val discountId: String,

//    @ColumnInfo(name = "tenant_id")
//    val tenantId: String,

    @ColumnInfo(name = "store_id")
    val storeId: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "type")
    val type: String, // 예: 'PERCENTAGE', 'FIXED_AMOUNT'

    @ColumnInfo(name = "value")
    val value: Double, // NUMERIC을 Double로 매핑

    @ColumnInfo(name = "sequence_in_display")
    val sequenceInDisplay: Long,

//    @ColumnInfo(name = "created_at")
//    val createdAt: Long,
//
//    @ColumnInfo(name = "updated_at")
//    val updatedAt: Long,
//
//    @ColumnInfo(name = "deleted_at")
//    val deletedAt: Long?
)


@Entity(
    tableName = "order_detail_discount",
    primaryKeys = ["order_detail_id", "discount_id"],
    foreignKeys = [
        ForeignKey(
            entity = OrderDetail::class,
            parentColumns = ["order_detail_id"],
            childColumns = ["order_detail_id"]
        ),
        ForeignKey(
            entity = Discount::class,
            parentColumns = ["discount_id"],
            childColumns = ["discount_id"]
        )
    ],
    indices = [Index("order_detail_id"), Index("discount_id")]
)
/*
* 주문한 상품 별 할인 정보
* */
data class OrderDetailDiscount(
    @ColumnInfo(name = "order_detail_id")
    val orderDetailId: String,

    @ColumnInfo(name = "discount_id")
    val discountId: String,

    @ColumnInfo(name = "created_at")
    val createdAt: Long
)

@Entity(
    tableName = "order_session_discount",
    primaryKeys = ["order_session_id", "discount_id"],
    foreignKeys = [
        ForeignKey(
            entity = OrderSession::class,
            parentColumns = ["order_session_id"],
            childColumns = ["order_session_id"]
        ),
        ForeignKey(
            entity = Discount::class,
            parentColumns = ["discount_id"],
            childColumns = ["discount_id"]
        )
    ],
    indices = [Index("order_session_id"), Index("discount_id")]
)
/*
* 주문 총금액 할인 정보
* */
data class OrderSessionDiscount(
    @ColumnInfo(name = "order_session_id")
    val orderSessionId: String,

    @ColumnInfo(name = "discount_id")
    val discountId: String,

    @ColumnInfo(name = "created_at")
    val createdAt: Long
)