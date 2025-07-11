

package com.duckduckgo.networkprotection.store.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NetPGeoswitchingDao {
    @Query("SELECT * from netp_geoswitching_location")
    fun getLocations(): List<NetPGeoswitchingLocation>

    @Query("SELECT * from netp_geoswitching_location")
    fun getLocationsFlow(): Flow<List<NetPGeoswitchingLocation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIntoLocations(locations: List<NetPGeoswitchingLocation>)

    @Query("DELETE from netp_geoswitching_location")
    fun deleteLocations()
}
