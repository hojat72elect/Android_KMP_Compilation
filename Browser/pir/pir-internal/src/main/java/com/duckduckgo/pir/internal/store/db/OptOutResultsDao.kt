

package com.duckduckgo.pir.internal.store.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface OptOutResultsDao {
    @Query("SELECT * FROM pir_opt_out_action_log ORDER BY completionTimeInMillis")
    fun getOptOutActionLogFlow(): Flow<List<OptOutActionLog>>

    @Query("SELECT * FROM pir_opt_out_complete_brokers ORDER BY endTimeInMillis")
    fun getOptOutCompletedBrokerFlow(): Flow<List<OptOutCompletedBroker>>

    @Query("SELECT * FROM pir_opt_out_complete_brokers ORDER BY endTimeInMillis")
    suspend fun getAllOptOutCompletedBrokers(): List<OptOutCompletedBroker>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOptOutActionLog(optOutActionLog: OptOutActionLog)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOptOutCompletedBroker(optOutCompletedBroker: OptOutCompletedBroker)

    @Query("DELETE from pir_opt_out_action_log")
    fun deleteAllOptOutActionLog()

    @Query("DELETE from pir_opt_out_complete_brokers")
    fun deleteAllOptOutCompletedBroker()
}
