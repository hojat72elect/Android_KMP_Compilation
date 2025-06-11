

package com.duckduckgo.mobile.android.vpn.dao

import androidx.room.*
import com.duckduckgo.mobile.android.vpn.model.VpnServiceStateStats
import kotlinx.coroutines.flow.Flow

@Dao
interface VpnServiceStateStatsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(stat: VpnServiceStateStats)

    @Query("SELECT * FROM vpn_service_state_stats ORDER BY timestamp DESC limit 1")
    fun getLastStateStats(): VpnServiceStateStats?

    @Query("SELECT * FROM vpn_service_state_stats ORDER BY timestamp DESC limit 1")
    fun getStateStats(): Flow<VpnServiceStateStats?>
}
