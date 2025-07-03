package com.eattalk.table.room.repository

import com.eattalk.table.room.Discount
import com.eattalk.table.room.dao.DiscountDao
import com.eattalk.table.room.util.Trigger
import com.eattalk.table.room.util.mergeTrigger
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class DiscountRepository(
    val discountDao: DiscountDao,
    val trigger: Trigger
) {
    fun getDiscounts(storeId: String) = trigger.discountTrigger.flatMapLatest {
        discountDao.getDiscounts(storeId)
    }

    fun getDiscounts(discountIds: List<String>) = trigger.discountTrigger.flatMapLatest {
        discountDao.getDiscounts(discountIds)
    }
    
    fun getDiscountsForOrderDetails(orderDetailIds: List<String>) =
        mergeTrigger(
            trigger.discountTrigger,
            trigger.orderDetailDiscountTrigger
        ).flatMapLatest{
            discountDao.getDiscountsForOrderDetails(orderDetailIds)
        }


    fun getDiscountsForOrderSession(sessionId: String) =
        mergeTrigger(
            trigger.discountTrigger,
            trigger.orderSessionDiscountTrigger
        ).flatMapLatest {
            discountDao.getDiscountsForOrderSession(sessionId)
        }

    suspend fun upsertDiscounts(discount: Discount) {
        discountDao.upsertDiscounts(discount)
        trigger.updateDiscountTrigger()
    }

    suspend fun deleteDiscount(discountId: String) {
        discountDao.deleteDiscount(discountId)
        trigger.updateDiscountTrigger()
    }

    suspend fun updateSequences(pairs: List<Pair<String, Int>>) {
        discountDao.updateSequences(pairs)
        trigger.updateDiscountTrigger()
    }
}