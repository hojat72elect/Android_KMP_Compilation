

package com.duckduckgo.app.trackerdetection.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity(tableName = "web_trackers_blocked")
data class WebTrackerBlocked(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val trackerUrl: String,
    val trackerCompany: String,
    val timestamp: String = timestamp(),
) {

    companion object {
        private val FORMATTER_SECONDS: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

        fun timestamp(date: LocalDateTime = LocalDateTime.now()): String {
            return FORMATTER_SECONDS.format(date)
        }
    }
}
