

package com.duckduckgo.sync.store.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.duckduckgo.sync.store.model.SyncAttempt
import kotlinx.coroutines.flow.Flow

@Dao
interface SyncAttemptDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(attempt: SyncAttempt)

    @Query("SELECT * FROM sync_attempts ORDER BY id DESC LIMIT 1")
    fun lastAttemptSync(): SyncAttempt?

    @Query("DELETE from sync_attempts")
    fun clear()

    @Query("SELECT * FROM sync_attempts ORDER BY id DESC LIMIT 1")
    fun lastAttempt(): Flow<SyncAttempt?>

    @Query("SELECT * FROM sync_attempts ORDER BY id DESC")
    fun allAttempts(): List<SyncAttempt>
}
