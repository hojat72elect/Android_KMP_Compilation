

package com.duckduckgo.pir.internal.store.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanResultsDao {
    @Query("SELECT * FROM pir_scan_complete_brokers ORDER BY endTimeInMillis")
    fun getScanCompletedBrokerFlow(): Flow<List<ScanCompletedBroker>>

    @Query("SELECT * FROM pir_scan_complete_brokers ORDER BY endTimeInMillis")
    suspend fun getAllScanCompletedBrokers(): List<ScanCompletedBroker>

    @Query("SELECT * FROM pir_scan_navigate_results ORDER BY completionTimeInMillis")
    fun getAllNavigateResultsFlow(): Flow<List<ScanNavigateResult>>

    @Query("SELECT * FROM pir_scan_navigate_results ORDER BY completionTimeInMillis")
    fun getAllNavigateResults(): List<ScanNavigateResult>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNavigateResult(scanNavigateResult: ScanNavigateResult)

    @Query("SELECT * FROM pir_scan_error ORDER BY completionTimeInMillis")
    fun getAllScanErrorResultsFlow(): Flow<List<ScanErrorResult>>

    @Query("SELECT * FROM pir_scan_error ORDER BY completionTimeInMillis")
    fun getAllScanErrorResults(): List<ScanErrorResult>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertScanErrorResult(scanErrorResult: ScanErrorResult)

    @Query("SELECT * FROM pir_scan_extracted_profile ORDER BY completionTimeInMillis")
    fun getAllExtractProfileResultFlow(): Flow<List<ExtractProfileResult>>

    @Query("SELECT * FROM pir_scan_extracted_profile ORDER BY completionTimeInMillis")
    fun getAllExtractProfileResult(): List<ExtractProfileResult>

    @Query("SELECT * FROM pir_scan_extracted_profile WHERE brokerName = :brokerName ORDER BY completionTimeInMillis")
    fun getExtractProfileResultForProfile(brokerName: String): List<ExtractProfileResult>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExtractProfileResult(extractProfileResult: ExtractProfileResult)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertScanCompletedBroker(scanCompletedBroker: ScanCompletedBroker)

    @Query("DELETE from pir_scan_navigate_results")
    fun deleteAllNavigateResults()

    @Query("DELETE from pir_scan_error")
    fun deleteAllScanErrorResults()

    @Query("DELETE from pir_scan_extracted_profile")
    fun deleteAllExtractProfileResult()

    @Query("DELETE from pir_scan_complete_brokers")
    fun deleteAllScanCompletedBroker()
}
