package com.eattalk.table.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.eattalk.table.room.Category
import kotlinx.coroutines.flow.Flow

/**
 * DAO for managing categories and their translations.
 */
@Dao
interface CategoryManagementDao {

    /**
     * 스토어에 속한 모든 카테고리와
     *   각 카테고리가 참조하는 Translation + TranslationLanguage 리스트를
     * @Transaction 보장하에 한 번에 가져옵니다.
     */
    @Transaction
    @Query(
        """
      SELECT *
        FROM category
       WHERE store_id = :storeId
       ORDER BY sequence_in_display
    """
    )
    fun getCategoriesWithTranslations(
        storeId: String
    ): Flow<List<Category>>


    @Transaction
    suspend fun addCategory(
        category: Category,
    ) {
        insertCategory(category)
    }


    @Transaction
    suspend fun deleteCategory(
        id: String,
    ) {
        softDeleteCategory(id)
    }


    @Insert
    suspend fun insertCategory(category: Category)



    @Update
    suspend fun updateCategory(category: Category)


    @Query("DELETE FROM category WHERE category_id = :categoryId")
    suspend fun softDeleteCategory(categoryId: String)

    @Query("""
      UPDATE category
      SET sequence_in_display = :sequence
      WHERE category_id = :categoryId
    """)
    suspend fun updateSequenceInDisplay(
        categoryId: String,
        sequence: Int
    ): Int

    /** 여러 건을 한 트랜잭션 안에서 순차적으로 업데이트 */
    @Transaction
    suspend fun updateSequences(pairs: List<Pair<String, Int>>) {
        pairs.forEach { (id, seq) ->
            updateSequenceInDisplay(id, seq)
        }
    }
}