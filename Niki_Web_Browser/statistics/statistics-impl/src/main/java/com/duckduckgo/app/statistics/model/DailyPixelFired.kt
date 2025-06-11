

package com.duckduckgo.app.statistics.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "daily_pixels_fired")
data class DailyPixelFired(
    @PrimaryKey val name: String,
    val date: LocalDate,
)
