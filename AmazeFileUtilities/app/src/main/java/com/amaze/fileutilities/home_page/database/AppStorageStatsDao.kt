

package com.amaze.fileutilities.home_page.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import java.util.Date

@Dao
interface AppStorageStatsDao {

    @Query("SELECT * FROM AppStorageStats")
    fun findAll(): List<AppStorageStats>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(appStorageStats: List<AppStorageStats>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(appStorageStats: AppStorageStats)

    /**
     * Inserts a new [AppStorageStats] associated with the [packageName] and containing [timestamp]
     * and [size]
     */
    @Transaction
    fun insert(packageName: String, timestamp: Date, size: Long) {
        val appStorageStats = AppStorageStats(packageName, timestamp, size)
        insert(appStorageStats)
    }

    @Query("DELETE FROM AppStorageStats WHERE timestamp < :date")
    fun deleteOlderThan(date: Date)

    @Query("SELECT * FROM AppStorageStats WHERE package_name=:packageName")
    fun findByPackageName(packageName: String): List<AppStorageStats>

    @Query(
        "SELECT * FROM AppStorageStats " +
            "WHERE package_name = :packageName " +
            "AND timestamp >= :periodStart " + // Ensure that timestamp is after `periodStart`
            "AND timestamp < :periodEnd " + // Ensure that timestamp is before `periodEnd`
            "ORDER BY timestamp ASC LIMIT 1" // Get the oldest entry based on timestamp
    )
    fun findOldestWithinPeriod(
        packageName: String,
        periodStart: Date,
        periodEnd: Date
    ): AppStorageStats?
}
