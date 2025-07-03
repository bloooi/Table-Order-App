package com.eattalk.table

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.eattalk.table.room.Option
import com.eattalk.table.room.OptionGroup
import com.eattalk.table.room.Order
import com.eattalk.table.room.OrderDetail
import com.eattalk.table.room.OrderDetailOption
import com.eattalk.table.room.OrderSession
import com.eattalk.table.room.Product
import com.eattalk.table.room.Seat
import com.eattalk.table.room.Zone
import com.eattalk.table.room.dao.TableDao
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class SeatWithSessionsTest : RoomDaoTest() {

    private lateinit var dao: TableDao

    @Before fun initDao() { dao = db.tableDao() }

    @Test
    fun load_seat_with_nested_orders() = runTest {
        /* ---------- given (트리 전체 insert) ---------- */
        // ① FOREIGN KEY 참조를 위한 Product 삽입
        val product = Product(
            productId     = "product_1",
            translations = mapOf("ko" to "상품1", "en" to "Product1"),
            imageUrl    = "res_1",
            price         = 1000.0,
            backgroundColor = "#FFFFFF",
            outOfStock    = false,
        )
        val zone = Zone("zone_1","store_1","홀1",0)
        val seat = Seat(
            seatId = "seat_1",
            zoneId = "zone_1",
            name = "테이블#1",
            x = 0,
            y = 0,
            width = 10,
            height = 10
        )
        val session = OrderSession(
            orderSessionId = "sess_1",
            seatId = "seat_1",
            type = "DINE_IN",
            headCount = 2,
            enteredAt = 0
        )
        val order   = Order(
            orderId = "order_1",
            storeId = "store_1",
            orderSessionId = "sess_1",
            sequence = 1,
            state = "RECEIVED",
            createdAt = 0,
        )
        val detail  = OrderDetail(
            orderDetailId = "detail_1",
            orderId = "order_1",
            productId = "product_1",
            quantity = 2,
            delivered = false
        )
        val optionGroup = OptionGroup(
            optionGroupId = "group_1",
            storeId       = "store_1",
            translations  = mapOf("ko" to "그룹1"),
            type          = "SELECTION",
            required      = false,
            maxQuantity   = 1,
            // createdAt/updatedAt/deletedAt 필드는 nullable 또는 default 처리된 경우 생략
        )

        val option = Option(
            optionId            = "option_1",
            optionGroupId       = "group_1",       // 사용하지 않는 값이어도 FK로 묶여있으면 반드시 넣어야 합니다
            translations        = mapOf("ko" to "옵션1"),
            price               = 100.0,
            sequenceInDisplay   = 0,
            default             = false,
            maxQuantity         = 1,
            outOfStock          = false,
        )

        val optMap  = OrderDetailOption(
            orderDetailId = "detail_1",
            optionId = "option_1",
            quantity = 1,
//            createdAt = 0,
//            updatedAt = 0,
//            deletedAt = null
        )

        db.productDao().upsertProduct(product)
        db.zoneDao().upsertZone(zone)
        db.tableDao().upsertSeat(seat)
        db.orderDao().upsertOrderSession(session)
        db.orderDao().upsertOrder(order)
        db.orderDao().upsertOrderDetail(detail)
        db.optionGroupDao().upsertOptionGroup(optionGroup)
        db.optionGroupDao().upsertOption(option)
        db.orderDao().upsertOrderDetailOption(optMap)

        /* ---------- when ---------- */
        val result = dao.getSeatWithSessions("seat_1").first()

        /* ---------- then ---------- */
        assertNotNull(result)
        assertEquals("테이블#1", result!!.seat.name)
        val loadedDetail = result.sessions.first()
            .orders.first()
            .details.first()
        assertEquals("detail_1", loadedDetail.detail.orderDetailId)
        // 옵션까지
        assertEquals(1, loadedDetail.options.size)
    }
}