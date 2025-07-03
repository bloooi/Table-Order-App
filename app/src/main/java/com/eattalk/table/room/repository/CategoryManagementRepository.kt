package com.eattalk.table.room.repository

import com.eattalk.table.room.Category
import com.eattalk.table.room.dao.CategoryManagementDao
import com.eattalk.table.room.util.Trigger
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class CategoryManagementRepository(
    val categoryManagementDao: CategoryManagementDao,
    val trigger: Trigger,
) {
    fun getCategoriesWithTranslations(storeId: String) = trigger.categoryTrigger.flatMapLatest {
        categoryManagementDao.getCategoriesWithTranslations(storeId)
    }
    suspend fun addCategory(category: Category) {
        categoryManagementDao.addCategory(category)
        trigger.updateCategoryTrigger()
    }
    suspend fun deleteCategory(id: String) {
        categoryManagementDao.deleteCategory(id)
        trigger.updateCategoryTrigger()
    }
    suspend fun updateCategory(category: Category){
        categoryManagementDao.updateCategory(category)
        trigger.updateCategoryTrigger()
    }

    suspend fun updateCategorySequences(pairs: List<Pair<String, Int>>) {
        categoryManagementDao.updateSequences(pairs)
        trigger.updateCategoryTrigger()
    }
}