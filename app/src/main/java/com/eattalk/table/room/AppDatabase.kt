package com.eattalk.table.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.room.TypeConverters
import com.eattalk.table.api.convert.toEntity
import com.eattalk.table.api.convert.toWithOptionsEntity
import com.eattalk.table.api.convert.toWithSeatsEntity
import com.eattalk.table.api.response.StoreFullEntry
import com.eattalk.table.api.util.toProductWithSeqList
import com.eattalk.table.room.dao.*         // CategoryDao, DiscountDao, OptionGroupDao, OrderDao, ProductDao, TableDao, ZoneDao...

@Database(
    entities = [
        // Room.kt 에 정의된 모든 @Entity
//        Store::class,
        Zone::class,
        Seat::class,
        Category::class,
        CategoryProduct::class,
        Product::class,
        ProductTag::class,
        OptionGroup::class,
        Option::class,
        ProductOptionGroup::class,
        Discount::class,
        OrderDetail::class,
        Order::class,
        OrderSession::class,
        OrderDetailOption::class,
        OrderDetailDiscount::class,
        OrderSessionDiscount::class,
    ],
    views = [
        OptionWithQuantityView::class
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    // 1) 카테고리·상품 관련
    abstract fun categoryManagementDao(): CategoryManagementDao

    // 2) 옵션 그룹·옵션 관련
    abstract fun optionGroupDao(): OptionGroupDao

    // 3) 상품 풀 스펙 관련
    abstract fun productDao(): ProductDao

    // 4) 주문 관련
    abstract fun orderDao(): OrderDao

    // 5) 테이블·세션·좌석 관련
    abstract fun tableDao(): TableDao

    // 6) 구역(Zone) 관련
    abstract fun zoneDao(): ZoneDao

    // 7) 할인 관련
    abstract fun discountDao(): DiscountDao

    @Transaction
    open suspend fun syncAllData(storeId: String, entry: StoreFullEntry) {
        clearAllTables()
        entry.optionGroups.forEach {
            optionGroupDao().upsertOptionGroupWithOptions(
                it.toWithOptionsEntity(storeId)
            )
        }

        entry.categories.forEachIndexed { index, categoryEntry ->
            categoryManagementDao().addCategory(categoryEntry.toEntity(storeId, index.toLong()))
        }

//        entry.discounts.forEachIndexed { index, discountEntry ->
//            discountDao().upsertDiscounts(discountEntry.toEntity(storeId, index.toLong()))
//        }

        entry.toProductWithSeqList().forEach {
            productDao().upsertProduct(it.product.toEntity())
            it.categoryId?.let { categoryId ->
                productDao().upsertCategoryProduct(
                    CategoryProduct(
                        categoryId = categoryId,
                        productId = it.product.productId,
                        sequenceInDisplay = it.sequenceInDisplay,
                    )
                )
            }

            it.product.optionGroups?.map { id ->
                productDao().upsertProductOptionGroup(
                    ProductOptionGroup(
                        productId = it.product.productId,
                        optionGroupId = id,
                    )
                )
            }

            it.product.tags?.map { tag ->
                productDao().upsertProductTag(
                    ProductTag(
                        productId = it.product.productId,
                        tag = tag,
                    )
                )
            }
        }
        entry.zones.forEachIndexed { index, zoneEntry ->
            val entity = zoneEntry.toWithSeatsEntity(
                storeId = storeId,
                sequence = index.toLong()
            )
            zoneDao().upsertZone(entity.zone)
            tableDao().upsertSeats(entity.seats)
        }

//        entry.orderSessions.map {
//            it.toSessionWithOrderEntities(storeId)
//        }.forEach {
//            orderDao().upsertOrderSession(it.orderSession)
//            orderDao().upsertOrders(it.orders)
//            orderDao().upsertOrderDetails(it.orderDetails)
//            orderDao().upsertOrderDetailOptions(it.detailOptions)
//        }
    }

}