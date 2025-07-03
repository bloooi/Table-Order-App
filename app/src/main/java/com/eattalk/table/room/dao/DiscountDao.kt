package com.eattalk.table.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.eattalk.table.room.Discount
import com.eattalk.table.room.DiscountWithDetailId
import kotlinx.coroutines.flow.Flow

/**
 * DAO for managing discounts.
 */
@Dao
interface DiscountDao {

    @Query("SELECT * FROM discount WHERE store_id = :storeId ORDER BY sequence_in_display")
    fun getDiscounts(storeId: String): Flow<List<Discount>>

    @Query("SELECT * FROM discount WHERE discount_id IN (:discountIds) ORDER BY sequence_in_display")
    fun getDiscounts(discountIds: List<String>): Flow<List<Discount>>
    /**
     * 주어진 orderDetailIds 에 매핑된
     * 각 order_detail_id 와 Discount 엔티티를 함께 반환합니다.
     */
    @Query("""
    SELECT
      odd.order_detail_id AS order_detail_id,
      d.* 
    FROM discount AS d
    JOIN order_detail_discount AS odd
      ON d.discount_id = odd.discount_id
    WHERE odd.order_detail_id IN (:orderDetailIds)
    ORDER BY d.sequence_in_display
  """)
    fun getDiscountsForOrderDetails(
        orderDetailIds: List<String>
    ): Flow<List<DiscountWithDetailId>>

    /**
     * 특정 order_session 에 적용된 전체 할인 목록을 Flow로 관찰합니다.
     *
     * @param sessionId 조회할 order_session_id
     * @return Flow emitting List<Discount> 세션 전체 할인에 매핑된 Discount 엔티티들
     */

    @Query(
        """
    SELECT d.*
      FROM discount AS d
      JOIN order_session_discount AS osd
        ON d.discount_id = osd.discount_id
     WHERE osd.order_session_id = :sessionId
     ORDER BY d.sequence_in_display
  """
    )
    fun getDiscountsForOrderSession(
        sessionId: String
    ): Flow<List<Discount>>

    @Upsert
    suspend fun upsertDiscounts(discount: Discount)

    @Query("DELETE FROM discount WHERE discount_id = :discountId")
    suspend fun deleteDiscount(discountId: String)

    @Query("""
      UPDATE discount
      SET sequence_in_display = :sequence
      WHERE discount_id = :discountId
    """)
    suspend fun updateSequenceInDisplay(
        discountId: String,
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
