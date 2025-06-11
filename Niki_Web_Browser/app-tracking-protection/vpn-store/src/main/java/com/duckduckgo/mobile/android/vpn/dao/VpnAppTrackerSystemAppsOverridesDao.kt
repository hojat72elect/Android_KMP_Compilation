

package com.duckduckgo.mobile.android.vpn.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.duckduckgo.mobile.android.vpn.trackers.AppTrackerSystemAppOverrideListMetadata
import com.duckduckgo.mobile.android.vpn.trackers.AppTrackerSystemAppOverridePackage

@Dao
interface VpnAppTrackerSystemAppsOverridesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSystemAppOverrides(tracker: List<AppTrackerSystemAppOverridePackage>)

    @Transaction
    fun upsertSystemAppOverrides(
        systemAppOverrides: List<AppTrackerSystemAppOverridePackage>,
    ) {
        deleteSystemAppOverrides()
        insertSystemAppOverrides(systemAppOverrides)
    }

    @Query("DELETE from vpn_app_tracker_system_app_override_list")
    fun deleteSystemAppOverrides()

    @Query("SELECT * FROM vpn_app_tracker_system_app_override_list")
    fun getSystemAppOverrides(): List<AppTrackerSystemAppOverridePackage>

    @Query("SELECT * from vpn_app_tracker_system_app_override_list_metadata ORDER BY id DESC LIMIT 1")
    fun getSystemAppOverridesMetadata(): AppTrackerSystemAppOverrideListMetadata?
}
