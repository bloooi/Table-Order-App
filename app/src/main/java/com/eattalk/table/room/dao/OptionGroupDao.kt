package com.eattalk.table.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.eattalk.table.room.Option
import com.eattalk.table.room.OptionGroup
import com.eattalk.table.room.OptionGroupWithOptions
import kotlinx.coroutines.flow.Flow

/**
 * DAO for managing option groups and their options.
 */
@Dao
interface OptionGroupDao {
    /**
     * 특정 상품(productId)에 매핑된 옵션 그룹과
     *   • 그룹 자체의 번역＋언어
     *   • 그 그룹에 속한 옵션＋번역
     */
    @Transaction
    @Query("""
    SELECT * FROM option_group
     WHERE option_group_id IN (
       SELECT option_group_id
         FROM product_option_group
        WHERE product_id = :productId
     )
  """)
    fun getOptionGroupsWithOptions(
        productId: String
    ): Flow<List<OptionGroupWithOptions>>

    /**
     * 특정 옵션 그룹(optionGroupId)과
     *   • 그룹 자체의 번역＋언어 목록
     *   • 그 그룹에 속한 옵션 각각의 번역＋언어 목록
     *
     * 을 한 번에 가져옵니다.
     */
    @Transaction
    @Query("""
      SELECT *
        FROM option_group
       WHERE option_group_id = :optionGroupId
    """)
    fun getOptionGroupWithOptions(
        optionGroupId: String
    ): Flow<OptionGroupWithOptions>

    /**
     * 주어진 매장(storeId)에 속한 모든 옵션 그룹과
     *   각 그룹에 딸린 옵션 리스트를 한 번에 조회합니다.
     */

    @Transaction
    @Query(
        """
    SELECT * 
      FROM option_group
     WHERE store_id = :storeId
     ORDER BY option_group_id
  """
    )
    fun getOptionGroupsWithOptionsByStore(
        storeId: String
    ): Flow<List<OptionGroupWithOptions>>

    @Transaction
    suspend fun upsertOptionGroupWithOptions(optionGroupWithOptions: OptionGroupWithOptions){
        upsertOptionGroup(optionGroupWithOptions.optionGroup)
        optionGroupWithOptions.options.forEach {
            upsertOption(it)
        }
    }

    @Upsert
    suspend fun upsertOptionGroup(optionGroup: OptionGroup)

    @Upsert
    suspend fun upsertOption(option: Option)

    /** 개별 옵션 그룹에 속한 모든 Option 삭제 */
    @Query("DELETE FROM `option` WHERE option_group_id = :groupId")
    suspend fun deleteOptionsByGroupId(groupId: String): Int

    /** 옵션 그룹 자체 삭제 */
    @Query("DELETE FROM option_group WHERE option_group_id = :groupId")
    suspend fun deleteOptionGroupById(groupId: String): Int

    /**
     * 주어진 optionGroupId로 OptionGroup과 그에 속한 Option을 모두 삭제
     */
    @Transaction
    suspend fun deleteOptionGroupWithOptions(groupId: String) {
        // 1) 옵션부터 삭제 (FK 제약이 없다면 순서 상관없으나, 안전하게 먼저)
        deleteOptionsByGroupId(groupId)
        // 2) 옵션 그룹 삭제
        deleteOptionGroupById(groupId)
    }
}