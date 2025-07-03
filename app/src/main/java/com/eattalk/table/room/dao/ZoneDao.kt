package com.eattalk.table.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.eattalk.table.room.Zone
import kotlinx.coroutines.flow.Flow

@Dao
interface ZoneDao {

    @Query("""
        SELECT * FROM zone 
            WHERE store_id = :storeId 
            ORDER BY sequence_in_display
    """)
    fun getZones(storeId: String): Flow<List<Zone>>


    @Query(
        """
        SELECT * FROM zone 
            WHERE zone_id = :zoneId
    """
    )
    fun getZone(zoneId: String): Flow<Zone?>


    @Upsert
    suspend fun upsertZone(zone: Zone)

    @Query("""
        DELETE FROM zone 
        WHERE zone_id = :zoneId
    """)
    suspend fun deleteZone(zoneId: String)
}