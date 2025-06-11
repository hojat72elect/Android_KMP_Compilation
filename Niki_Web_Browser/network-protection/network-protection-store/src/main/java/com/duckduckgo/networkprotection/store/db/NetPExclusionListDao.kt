

package com.duckduckgo.networkprotection.store.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NetPExclusionListDao {
    @Query("SELECT * from netp_manual_app_exclusion_list")
    fun getManualAppExclusionList(): List<NetPManuallyExcludedApp>

    @Query("SELECT * from netp_manual_app_exclusion_list")
    fun getManualAppExclusionListFlow(): Flow<List<NetPManuallyExcludedApp>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIntoManualAppExclusionList(excludedApp: NetPManuallyExcludedApp)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIntoManualAppExclusionList(excludedApps: List<NetPManuallyExcludedApp>)

    @Query("DELETE from netp_manual_app_exclusion_list")
    fun deleteManualAppExclusionList()
}
