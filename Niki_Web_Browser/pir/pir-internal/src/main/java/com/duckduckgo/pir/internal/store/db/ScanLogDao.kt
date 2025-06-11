

package com.duckduckgo.pir.internal.store.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanLogDao {
    @Query("SELECT * FROM pir_events_log ORDER BY eventTimeInMillis")
    fun getAllEventLogsFlow(): Flow<List<PirEventLog>>

    @Query("SELECT * FROM pir_broker_scan_log ORDER BY eventTimeInMillis")
    fun getAllBrokerScanEventsFlow(): Flow<List<PirBrokerScanLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEventLog(pirScanLog: PirEventLog)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBrokerScanEvent(pirBrokerScanLog: PirBrokerScanLog)

    @Query("DELETE from pir_events_log")
    fun deleteAllEventLogs()

    @Query("DELETE from pir_broker_scan_log")
    fun deleteAllBrokerScanEvents()
}
