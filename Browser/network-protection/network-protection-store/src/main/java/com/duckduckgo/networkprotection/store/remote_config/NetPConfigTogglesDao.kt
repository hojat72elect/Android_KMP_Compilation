

package com.duckduckgo.networkprotection.store.remote_config

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NetPConfigTogglesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vpnConfigToggle: NetPConfigToggle)

    @Query("SELECT * from netp_config_toggles ORDER BY localtime DESC")
    fun getConfigToggles(): List<NetPConfigToggle>
}
