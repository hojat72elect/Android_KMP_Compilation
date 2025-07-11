

package com.duckduckgo.app.anr.internal.store

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface InternalANRDao {

    @Query("SELECT * FROM anr_events")
    fun getAnrs(): Flow<List<AnrInternalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnr(anr: AnrInternalEntity)

    @Query("DELETE FROM anr_events where timestamp < :removeBeforeTimestamp")
    suspend fun removeOldAnrs(removeBeforeTimestamp: String)
}
