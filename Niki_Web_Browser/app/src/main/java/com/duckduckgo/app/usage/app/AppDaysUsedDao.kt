

package com.duckduckgo.app.usage.app

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
abstract class AppDaysUsedDao {

    @Query("SELECT COUNT(*) from app_days_used")
    abstract fun getNumberOfDaysAppUsed(): Long

    @Query("SELECT COUNT(*) from app_days_used WHERE date > :isoDate")
    abstract fun getNumberOfDaysAppUsedSince(isoDate: String): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(appUsedEntity: AppDaysUsedEntity)

    @Query("SELECT * FROM app_days_used ORDER BY date DESC LIMIT 1")
    abstract fun getLastDayAppUsed(): AppDaysUsedEntity?
}
