

package com.duckduckgo.app.tabs.store

import com.duckduckgo.app.tabs.model.TabRepository
import com.duckduckgo.app.tabs.store.TabStatsBucketing.Companion.ACTIVITY_BUCKETS
import com.duckduckgo.app.tabs.store.TabStatsBucketing.Companion.ONE_WEEK_IN_DAYS
import com.duckduckgo.app.tabs.store.TabStatsBucketing.Companion.TAB_COUNT_BUCKETS
import com.duckduckgo.app.tabs.store.TabStatsBucketing.Companion.THREE_WEEKS_IN_DAYS
import com.duckduckgo.app.tabs.store.TabStatsBucketing.Companion.TWO_WEEKS_IN_DAYS
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface TabStatsBucketing {
    suspend fun getNumberOfOpenTabs(): String
    suspend fun getTabsActiveLastWeek(): String
    suspend fun getTabsActiveOneWeekAgo(): String
    suspend fun getTabsActiveTwoWeeksAgo(): String
    suspend fun getTabsActiveMoreThanThreeWeeksAgo(): String

    companion object {
        const val ONE_WEEK_IN_DAYS = 7L
        const val TWO_WEEKS_IN_DAYS = 14L
        const val THREE_WEEKS_IN_DAYS = 21L

        val TAB_COUNT_BUCKETS = listOf(
            0..1,
            2..5,
            6..10,
            11..20,
            21..40,
            41..60,
            61..80,
            81..Int.MAX_VALUE,
        )

        val ACTIVITY_BUCKETS = listOf(
            0..0,
            1..5,
            6..10,
            11..20,
            21..Int.MAX_VALUE,
        )
    }
}

@ContributesBinding(AppScope::class)
class DefaultTabStatsBucketing @Inject constructor(
    private val tabRepository: TabRepository,
) : TabStatsBucketing {
    override suspend fun getNumberOfOpenTabs(): String {
        val count = tabRepository.getOpenTabCount()
        return getBucketLabel(count, TAB_COUNT_BUCKETS)
    }

    override suspend fun getTabsActiveLastWeek(): String {
        val count = tabRepository.countTabsAccessedWithinRange(accessOlderThan = 0, accessNotMoreThan = ONE_WEEK_IN_DAYS)
        return getBucketLabel(count, ACTIVITY_BUCKETS)
    }

    override suspend fun getTabsActiveOneWeekAgo(): String {
        val count = tabRepository.countTabsAccessedWithinRange(accessOlderThan = ONE_WEEK_IN_DAYS, accessNotMoreThan = TWO_WEEKS_IN_DAYS)
        return getBucketLabel(count, ACTIVITY_BUCKETS)
    }

    override suspend fun getTabsActiveTwoWeeksAgo(): String {
        val count = tabRepository.countTabsAccessedWithinRange(accessOlderThan = TWO_WEEKS_IN_DAYS, accessNotMoreThan = THREE_WEEKS_IN_DAYS)
        return getBucketLabel(count, ACTIVITY_BUCKETS)
    }

    override suspend fun getTabsActiveMoreThanThreeWeeksAgo(): String {
        val count = tabRepository.countTabsAccessedWithinRange(accessOlderThan = THREE_WEEKS_IN_DAYS)
        return getBucketLabel(count, ACTIVITY_BUCKETS)
    }

    private fun getBucketLabel(count: Int, buckets: List<IntRange>): String {
        val bucket = buckets.first { bucket ->
            count in bucket
        }
        return when (bucket) {
            buckets.first() -> {
                bucket.last.toString()
            }
            buckets.last() -> {
                "${bucket.first}+"
            }
            else -> {
                "${bucket.first}-${bucket.last}"
            }
        }
    }
}
