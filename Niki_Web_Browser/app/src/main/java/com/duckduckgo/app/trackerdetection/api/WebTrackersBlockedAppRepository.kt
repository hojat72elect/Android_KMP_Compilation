

package com.duckduckgo.app.trackerdetection.api

import com.duckduckgo.app.global.db.AppDatabase
import com.duckduckgo.app.trackerdetection.db.WebTrackerBlocked
import com.duckduckgo.common.utils.formatters.time.DatabaseDateFormatter
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import java.time.LocalDateTime
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@ContributesBinding(AppScope::class)
class WebTrackersBlockedAppRepository @Inject constructor(appDatabase: AppDatabase) : WebTrackersBlockedRepository {

    private val dao = appDatabase.webTrackersBlockedDao()

    override fun get(
        startTime: () -> String,
        endTime: String,
    ): Flow<List<WebTrackerBlocked>> {
        return dao.getTrackersBetween(startTime(), endTime)
            .distinctUntilChanged()
            .map { it.filter { tracker -> tracker.timestamp >= startTime() } }
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }

    // TODO move to public API if experiment kept
    suspend fun getTrackerCountForLast7Days(): Int {
        return getTrackersCountBetween(
            startTime = LocalDateTime.now().minusDays(7),
            endTime = LocalDateTime.now(),
        )
    }

    private suspend fun getTrackersCountBetween(
        startTime: LocalDateTime,
        endTime: LocalDateTime,
    ): Int = dao.getTrackersCountBetween(
        startTime = DatabaseDateFormatter.timestamp(startTime),
        endTime = DatabaseDateFormatter.timestamp(endTime),
    )
}
