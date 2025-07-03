package com.eattalk.table.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.eattalk.table.room.Seat
import com.eattalk.table.room.SeatWithSessions
import com.eattalk.table.room.SessionFull
import com.eattalk.table.room.ZoneWithSeats
import com.eattalk.table.room.ZoneWithSeatsAndSessions
import kotlinx.coroutines.flow.Flow


@Dao
interface TableDao {
    /**
     * Get all zones (areas) and their seats for a store.
     */

    @Transaction
    @Query(
        "SELECT * FROM zone"
                + " WHERE store_id = :storeId"
                + " ORDER BY sequence_in_display"
    )
    fun getZonesWithSeats(storeId: String): Flow<List<ZoneWithSeats>>


    /**
     * storeId에 속한 모든 구역(zone)과,
     *   └ 각 구역의 좌석(seat)과,
     *       └ 각 좌석의 “진행 중인(order_session.exited_at IS NULL)” 세션,
     *           └ 그 세션의 모든 주문(order) 및 주문 상세(order_detail)
     *
     * 이 한 번의 @Transaction 호출로 전부 내려받습니다.
     */
    @Transaction
    @Query(
        """
    SELECT * FROM zone
     WHERE store_id = :storeId
     ORDER BY sequence_in_display
  """
    )

    fun getZonesWithSeatsAndSessions(
        storeId: String
    ): Flow<List<ZoneWithSeatsAndSessions>>

    /**
     * 주어진 seatId 에 대한
     *  • Seat 엔티티
     *  • 해당 좌석의 모든 OrderSession
     *  • 각 세션의 모든 Order + OrderDetail + Option
     *
     * 을 한 번의 @Transaction 으로 내려받습니다.
     */

    @Transaction
    @Query(
        """
      SELECT *
        FROM seat
       WHERE seat_id = :seatId
    """
    )
    fun getSeatWithSessions(
        seatId: String
    ): Flow<SeatWithSessions?>


    @Transaction
    @Query(
        """
    SELECT * 
      FROM order_session
     WHERE seat_id = :seatId
  """
    )
    fun getActiveSessionFull(seatId: String): Flow<SessionFull?>

    @Query("""
        DELETE FROM seat 
        WHERE zone_id = :zoneId
    """)
    suspend fun deleteSeats(zoneId: String)

    @Upsert
    suspend fun upsertSeat(seat: Seat)

    @Upsert
    suspend fun upsertSeats(seats: List<Seat>)
}