package com.eattalk.table.room.repository

import com.eattalk.table.room.Order
import com.eattalk.table.room.OrderDetail
import com.eattalk.table.room.OrderDetailOption
import com.eattalk.table.room.OrderSession
import com.eattalk.table.room.dao.OrderDao
import com.eattalk.table.room.util.Trigger
import com.eattalk.table.room.util.mergeTrigger
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class OrderRepository(
    val orderDao: OrderDao,
    val trigger: Trigger,
) {
    fun getOrders(storeId: String) =
        mergeTrigger(
            trigger.orderTrigger,
            trigger.orderDetailTrigger,
            trigger.orderDetailOptionTrigger,
            trigger.productTrigger,
            trigger.optionTrigger,
            trigger.orderSessionTrigger,
            trigger.seatTrigger
        ).flatMapLatest {
            orderDao.getAllOrdersWithDetails(storeId)
        }
    fun getOrderSessionFull(sessionId: String) =
        mergeTrigger(
            trigger.orderSessionTrigger,
            trigger.orderTrigger,
            trigger.orderDetailTrigger,
            trigger.orderDetailOptionTrigger,
            trigger.productTrigger,
            trigger.optionTrigger,
            trigger.seatTrigger
        ).flatMapLatest {
            orderDao.getOrderSessionFull(sessionId)
        }

    suspend fun upsertOrder(order: Order) {
        orderDao.upsertOrder(order)
        trigger.updateOrderTrigger()
    }
    suspend fun upsertOrderSession(orderSession: OrderSession){
        orderDao.upsertOrderSession(orderSession)
        trigger.updateOrderSessionTrigger()
    }

    suspend fun upsertOrderDetail(orderDetail: OrderDetail) {
        orderDao.upsertOrderDetail(orderDetail)
        trigger.updateOrderDetailTrigger()
    }

    suspend fun upsertOrderDetails(details: List<OrderDetail>) {
        orderDao.upsertOrderDetails(details)
        trigger.updateOrderDetailTrigger()
    }

    suspend fun upsertOrderDetailOption(orderDetailOption: OrderDetailOption){
        orderDao.upsertOrderDetailOption(orderDetailOption)
        trigger.updateOrderDetailOptionTrigger()
    }

    suspend fun upsertOrderDetailOptions(options: List<OrderDetailOption>) {
        orderDao.upsertOrderDetailOptions(options)
        trigger.updateOrderDetailOptionTrigger()
    }

    suspend fun upsertFullSession(
        session: OrderSession,
        orders: List<Order>,
        details: List<OrderDetail>,
        options: List<OrderDetailOption>
    ) {
        orderDao.upsertFullSession(session, orders, details, options)
        trigger.updateOrderSessionTrigger()
        trigger.updateOrderTrigger()
        trigger.updateOrderDetailTrigger()
        trigger.updateOrderDetailOptionTrigger()
    }

    suspend fun upsertFullOrder(
        orders: Order,
        details: List<OrderDetail>,
        options: List<OrderDetailOption>
    ) {
        orderDao.upsertFullOrder(orders, details, options)
        trigger.updateOrderTrigger()
        trigger.updateOrderDetailTrigger()
        trigger.updateOrderDetailOptionTrigger()
    }

    // 이거 Flow 아닌데 괜찮나...?
    fun getOrdersByStore(storeId: String) = orderDao.getOrderIdsBySession(storeId)

    suspend fun updateOrderState(orderId: String, newState: String) {
        orderDao.updateOrderState(orderId, newState)
        trigger.updateOrderTrigger()
    }
    suspend fun deleteSessionAndOrders(sessionId: String){
        orderDao.deleteSessionAndOrders(sessionId)
        trigger.updateOrderSessionTrigger()
        trigger.updateOrderTrigger()
        trigger.updateOrderDetailTrigger()
        trigger.updateOrderDetailOptionTrigger()
    }

    suspend fun cancelAllOrders(sessionId: String) {
        orderDao.cancelAllOrders(sessionId)
        trigger.updateOrderTrigger()
    }

    suspend fun deleteOrderDetailFromId(detailId: String) {
        orderDao.deleteOrderDetailTransaction(detailId)
        trigger.updateOrderDetailTrigger()
        trigger.updateOrderDetailOptionTrigger()
    }
    
    suspend fun deleteOrderFromId(orderId: String) {
        orderDao.deleteOrderTransaction(orderId)
        trigger.updateOrderTrigger()
        trigger.updateOrderDetailTrigger()
        trigger.updateOrderDetailOptionTrigger()
    }
}