package com.eattalk.table.room.repository

import com.eattalk.table.room.Seat
import com.eattalk.table.room.dao.TableDao
import com.eattalk.table.room.util.Trigger
import com.eattalk.table.room.util.mergeTrigger
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class TableRepository (
    val tableDao: TableDao,
    val trigger: Trigger
) {
    fun getZonesWithSeats(storeId: String) =
        mergeTrigger(
            trigger.zoneTrigger,
            trigger.seatTrigger
        ).flatMapLatest{
            tableDao.getZonesWithSeats(storeId)
        }
    fun getZonesWithSeatsAndSessions(storeId: String) =
        mergeTrigger(
            trigger.zoneTrigger,
            trigger.seatTrigger,
            trigger.orderSessionTrigger,
            trigger.orderTrigger,
            trigger.orderDetailTrigger,
            trigger.orderDetailOptionTrigger,
            trigger.productTrigger,
            trigger.optionTrigger
        ).flatMapLatest {
            tableDao.getZonesWithSeatsAndSessions(storeId)
        }
    fun getSeatWithSessions(seatId: String) =
        mergeTrigger(
            trigger.seatTrigger,
            trigger.orderSessionTrigger,
            trigger.orderTrigger,
            trigger.orderDetailTrigger,
            trigger.orderDetailOptionTrigger,
            trigger.productTrigger,
            trigger.optionTrigger
        ).flatMapLatest {
            tableDao.getSeatWithSessions(seatId)
        }

    fun getActiveSession(seatId: String) =
        mergeTrigger(
            trigger.orderSessionTrigger,
            trigger.orderTrigger,
            trigger.orderDetailTrigger,
            trigger.orderDetailOptionTrigger,
            trigger.productTrigger,
            trigger.optionTrigger
        ).flatMapLatest {
            tableDao.getActiveSessionFull(seatId)
        }
    suspend fun deleteSeats(zoneId: String) {
        tableDao.deleteSeats(zoneId)
        trigger.updateSeatTrigger()
    }

    suspend fun upsertSeat(seat: Seat) {
        tableDao.upsertSeat(seat)
        trigger.updateSeatTrigger()
    }
}