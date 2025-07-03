package com.eattalk.table.room.repository

import com.eattalk.table.api.response.StoreFullEntry
import com.eattalk.table.room.AppDatabase
import com.eattalk.table.room.util.Trigger


class StoreRepository (
    private val appDatabase: AppDatabase,
    private val trigger: Trigger
){
    suspend fun syncAllDataFromServer(storeId: String, entry: StoreFullEntry){
        trigger.updateZoneTrigger()
        trigger.updateSeatTrigger()
        trigger.updateOrderSessionTrigger()
        trigger.updateOrderTrigger()
        trigger.updateOrderDetailTrigger()
        trigger.updateOrderDetailOptionTrigger()
        trigger.updateOptionGroupTrigger()
        trigger.updateOptionTrigger()
        trigger.updateProductTrigger()
        trigger.updateProductOptionGroupTrigger()
        trigger.updateProductTagTrigger()
        trigger.updateCategoryTrigger()
        trigger.updateCategoryProductTrigger()
        trigger.updateDiscountTrigger()
        trigger.updateOrderDetailDiscountTrigger()
        trigger.updateOrderSessionDiscountTrigger()

        appDatabase.syncAllData(storeId, entry)
    }

}