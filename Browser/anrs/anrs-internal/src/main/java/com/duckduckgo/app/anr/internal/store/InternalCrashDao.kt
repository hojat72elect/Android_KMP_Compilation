

package com.duckduckgo.app.anr.internal.store

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface InternalCrashDao {

    @Query("SELECT * FROM crash_events")
    fun getCrashes(): Flow<List<CrashInternalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrash(anr: CrashInternalEntity)

    @Query("DELETE FROM crash_events where timestamp < :removeBeforeTimestamp")
    fun removeOldCrashes(removeBeforeTimestamp: String)
}
