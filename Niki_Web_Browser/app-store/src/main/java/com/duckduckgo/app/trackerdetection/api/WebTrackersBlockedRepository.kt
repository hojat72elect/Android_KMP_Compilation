

package com.duckduckgo.app.trackerdetection.api

import com.duckduckgo.app.trackerdetection.db.DatabaseDateFormatter
import com.duckduckgo.app.trackerdetection.db.WebTrackerBlocked
import java.time.LocalDateTime
import kotlinx.coroutines.flow.Flow

interface WebTrackersBlockedRepository {

    fun get(
        startTime: () -> String,
        endTime: String = noEndDate(),
    ): Flow<List<WebTrackerBlocked>>

    suspend fun deleteAll()

    private fun noEndDate(): String {
        return DatabaseDateFormatter.timestamp(LocalDateTime.of(9999, 1, 1, 0, 0))
    }
}
