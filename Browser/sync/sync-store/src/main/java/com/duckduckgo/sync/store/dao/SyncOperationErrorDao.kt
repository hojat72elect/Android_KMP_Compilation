

package com.duckduckgo.sync.store.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.duckduckgo.sync.store.model.SyncOperationError

@Dao
interface SyncOperationErrorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(error: SyncOperationError)

    @Query("SELECT * FROM sync_operation_errors WHERE date = :date")
    fun errorsByDate(date: String): List<SyncOperationError>

    @Query("UPDATE sync_operation_errors SET count = count + 1 WHERE feature = :feature AND errorType = :error AND date = :date")
    fun incrementCount(feature: String, error: String, date: String)

    @Query("SELECT * FROM sync_operation_errors WHERE feature = :feature AND errorType = :error AND date = :date LIMIT 1")
    fun featureErrorByDate(feature: String, error: String, date: String): SyncOperationError?

    @Query("SELECT * FROM sync_operation_errors ORDER BY id DESC")
    fun allErrors(): List<SyncOperationError>
}
