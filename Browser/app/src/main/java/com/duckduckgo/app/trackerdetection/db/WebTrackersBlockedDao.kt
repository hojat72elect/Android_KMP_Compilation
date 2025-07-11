

package com.duckduckgo.app.trackerdetection.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WebTrackersBlockedDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(tracker: WebTrackerBlocked)

    @Query("DELETE FROM web_trackers_blocked")
    suspend fun deleteAll()

    @Query("DELETE FROM web_trackers_blocked WHERE timestamp < :startTime")
    fun deleteOldDataUntil(startTime: String)

    @Query("SELECT * FROM web_trackers_blocked WHERE timestamp >= :startTime AND timestamp < :endTime ORDER BY timestamp DESC")
    fun getTrackersBetween(
        startTime: String,
        endTime: String,
    ): Flow<List<WebTrackerBlocked>>

    @Query("SELECT COUNT(*) FROM web_trackers_blocked WHERE timestamp >= :startTime AND timestamp < :endTime")
    suspend fun getTrackersCountBetween(
        startTime: String,
        endTime: String,
    ): Int
}
