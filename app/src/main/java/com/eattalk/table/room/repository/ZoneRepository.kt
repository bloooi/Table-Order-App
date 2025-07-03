package com.eattalk.table.room.repository

import com.eattalk.table.room.Seat
import com.eattalk.table.room.Zone
import com.eattalk.table.room.dao.TableDao
import com.eattalk.table.room.dao.ZoneDao
import com.eattalk.table.room.util.Trigger
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class ZoneRepository(
    val zoneDao: ZoneDao,
    val tableDao: TableDao,
    val trigger: Trigger
) {
    fun getZones(storeId: String) =
        trigger.zoneTrigger.flatMapLatest {
            zoneDao.getZones(storeId)
        }
    fun getZone(zoneId: String) =
        trigger.zoneTrigger.flatMapLatest {
            zoneDao.getZone(zoneId)
        }

    suspend fun upsertZoneWithSeats(zone: Zone, seats: List<Seat>){
        zoneDao.upsertZone(zone)
        tableDao.upsertSeats(seats)
        trigger.updateZoneTrigger()
        trigger.updateSeatTrigger()

    }
    suspend fun deleteZone(zoneId: String){
        zoneDao.deleteZone(zoneId)
        tableDao.deleteSeats(zoneId)
        trigger.updateZoneTrigger()
        trigger.updateSeatTrigger()
    }

}