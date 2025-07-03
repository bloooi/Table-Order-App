package com.eattalk.table.room.repository

import com.eattalk.table.room.OptionGroupWithOptions
import com.eattalk.table.room.dao.OptionGroupDao
import com.eattalk.table.room.util.Trigger
import com.eattalk.table.room.util.mergeTrigger
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class OptionGroupRepository(
    val optionGroupDao: OptionGroupDao,
    val trigger: Trigger,
)  {
    fun getOptionGroupsWithOptions(productId: String) =
        mergeTrigger(
            trigger.optionGroupTrigger,
            trigger.optionTrigger
        ).flatMapLatest {
            optionGroupDao.getOptionGroupsWithOptions(productId)
        }
    fun getOptionGroupWithOptions(optionGroupId: String) =
        mergeTrigger(
            trigger.optionGroupTrigger,
            trigger.optionTrigger
        ).flatMapLatest {
            optionGroupDao.getOptionGroupWithOptions(optionGroupId)
        }
    fun getOptionGroupsWithOptionsByStore(storeId: String) =
        mergeTrigger(
            trigger.optionGroupTrigger,
            trigger.optionTrigger
        ).flatMapLatest {
            optionGroupDao.getOptionGroupsWithOptionsByStore(storeId)
        }

    suspend fun upsertOptionGroupWithOptions(optionGroupWithOptions: OptionGroupWithOptions) {
        optionGroupDao.upsertOptionGroupWithOptions(optionGroupWithOptions)
        trigger.updateOptionGroupTrigger()
        trigger.updateOptionTrigger()
    }
    suspend fun deleteOptionGroupWithOptions(groupId: String) {
        optionGroupDao.deleteOptionGroupWithOptions(groupId)
        trigger.updateOptionGroupTrigger()
        trigger.updateOptionTrigger()
    }
}