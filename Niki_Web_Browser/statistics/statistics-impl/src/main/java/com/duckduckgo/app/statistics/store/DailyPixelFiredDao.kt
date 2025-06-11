

package com.duckduckgo.app.statistics.store

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.duckduckgo.app.statistics.model.DailyPixelFired
import java.time.LocalDate

@Dao
interface DailyPixelFiredDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: DailyPixelFired)

    @Query("SELECT COUNT(1) > 0 FROM daily_pixels_fired WHERE name = :name AND date = :date")
    suspend fun hasDailyPixelFired(name: String, date: LocalDate): Boolean
}
