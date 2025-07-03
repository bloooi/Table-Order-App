package com.eattalk.table.room.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.eattalk.table.model.OrderType
import com.eattalk.table.room.Order
import com.eattalk.table.room.OrderDetail
import com.eattalk.table.room.OrderDetailOption
import com.eattalk.table.room.OrderSession
import com.eattalk.table.room.OrderWithDetails
import com.eattalk.table.room.OrderWithDetailsAndSessionWithSeats
import com.eattalk.table.room.SessionFull
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    /**
     * 세션과 상관없이, 모든 주문을 sequence 내림차순으로 불러오면서
     * 각 주문에 딸린 OrderDetail 리스트를 함께 조회합니다.
     */

    @Transaction
    @Query(
        """
      SELECT *
        FROM `order`
       WHERE store_id = :storeId
       ORDER BY sequence DESC
    """
    )
    fun getAllOrdersWithDetails(storeId: String): Flow<List<OrderWithDetailsAndSessionWithSeats>>

    @Transaction
    @Query(
        """
        SELECT * 
        FROM order_session
         WHERE order_session_id = :sessionId
        """
    )
    fun getOrderSessionFull(sessionId: String): Flow<SessionFull>

    @Transaction
    @Query(
        """
            SELECT *
            FROM `order`
            WHERE order_id = :orderId
        """
    )
    fun getOrderById(orderId: String): OrderWithDetails?

    @Query("UPDATE `order` SET state = :newState WHERE order_id = :orderId")
    suspend fun updateOrderState(orderId: String, newState: String)

    @Query("UPDATE `order` SET state = :state WHERE order_session_id = :orderSessionId")
    suspend fun cancelAllOrders(orderSessionId: String, state: String = OrderType.Canceled.key)

    @Upsert
    suspend fun upsertOrder(order: Order)

    @Upsert
    suspend fun upsertOrders(orders: List<Order>) // Order 리스트를 위한 upsert 추가

    @Upsert
    suspend fun upsertOrderSession(orderSession: OrderSession)

    @Upsert
    suspend fun upsertOrderDetail(orderDetail: OrderDetail)

    @Upsert
    suspend fun upsertOrderDetails(details: List<OrderDetail>) // OrderDetail 리스트를 위한 upsert 추가


    @Upsert
    suspend fun upsertOrderDetailOption(orderDetailOption: OrderDetailOption)

    @Upsert
    suspend fun upsertOrderDetailOptions(options: List<OrderDetailOption>) // OrderDetailOption 리스트를 위한 upsert 추가


    @Transaction
    suspend fun upsertFullSession(
        session: OrderSession,
        orders: List<Order>,
        details: List<OrderDetail>,
        options: List<OrderDetailOption>
    ) {
        Log.d("DB_DEBUG", "orders: ${orders.joinToString{it.orderId}}\ndetails : ${details.joinToString{"order:${it.orderId} - detail:${it.orderDetailId}"}}\n options: ${options.joinToString{"detail:${it.orderDetailId} - option:${it.optionId}"}}")

        Log.d("DB_DEBUG", "Upserting OrderSession: ${session.orderSessionId}")
        upsertOrderSession(session)

        Log.d("DB_DEBUG", "Upserting Orders: ${orders.joinToString{it.orderId}}, SessionId: ${orders.joinToString{"${it.orderId} - ${it.orderSessionId}"}}")
        upsertOrders(orders)

        Log.d("DB_DEBUG", "Upserting OrderDetail: ${details.joinToString{it.orderDetailId}}, OrderId: ${details.joinToString{it.orderId}}, ProductId: ${details.joinToString{it.productId}}")
        upsertOrderDetails(details)
        upsertOrderDetailOptions(options)
    }

    @Transaction
    suspend fun upsertFullOrder(
        orders: Order,
        details: List<OrderDetail>,
        options: List<OrderDetailOption>
    ){
        upsertOrder(orders)
        upsertOrderDetails(details)
        upsertOrderDetailOptions(options)
    }

    @Query("DELETE FROM order_session WHERE order_session_id = :sessionId")
    suspend fun deleteOrderSession(sessionId: String)

    @Query("DELETE FROM `order` WHERE order_session_id = :sessionId")
    suspend fun deleteOrder(sessionId: String)

    @Query("DELETE FROM order_detail WHERE order_id = :orderId")
    suspend fun deleteOrderDetail(orderId: String)

    @Query("DELETE FROM order_detail WHERE order_detail_id = :orderDetailId")
    suspend fun deleteOrderDetailFromId(orderDetailId: String)

    @Query("DELETE FROM order_detail_option WHERE order_detail_id = :orderDetailId")
    suspend fun deleteOrderDetailOption(orderDetailId: String)

    @Transaction
    suspend fun deleteOrderDetailTransaction(orderDetailId: String) {
        deleteOrderDetailOption(orderDetailId)
        deleteOrderDetailFromId(orderDetailId)
    }

    @Transaction
    suspend fun deleteOrderTransaction(orderId: String) {
        val entity = getOrderById(orderId)
        entity?.details?.forEach {
            deleteOrderDetailOption(it.detail.orderDetailId)
        }
        deleteOrderDetail(orderId)
        deleteOrder(orderId)

    }
    /**
     * 해당 세션에 속한 모든 order_id 를 조회
     * (한 세션에 여러 주문이 있을 수 있기 때문에)
     */
    @Query("SELECT order_id FROM `order` WHERE order_session_id = :sessionId")
    fun getOrderIdsBySession(sessionId: String): List<String>

    /**
     * 0) order_detail_option
     * 1) order_detail
     * 2) order
     * 3) order_session
     * 순으로 모두 삭제하는 Transaction
     */
    @Transaction
    suspend fun deleteSessionAndOrders(sessionId: String) {
        // 1) order_detail 먼저 삭제
        getOrderIdsBySession(sessionId).forEach { orderId ->
            val entity = getOrderById(orderId)
            entity?.details?.forEach {
                deleteOrderDetailOption(it.detail.orderDetailId)
            }
            deleteOrderDetail(orderId)
        }
        // 2) order 삭제
        deleteOrder(sessionId)
        // 3) session 삭제
        deleteOrderSession(sessionId)
    }

    @Transaction
    suspend fun deleteAllOrders(sessionId: String) {

    }
}