

package com.duckduckgo.app.usage.app

import java.util.*

/**
 * Repository for storing and retrieving the number of days the app has been used
 */
interface AppDaysUsedRepository {

    /**
     * Get the number of days the app has been used
     */
    suspend fun getNumberOfDaysAppUsed(): Long

    /**
     * Record that the app has been used today
     */
    suspend fun recordAppUsedToday()

    /**
     * Get the number of days the app has been used since a given date.
     *
     * The provided [date] is compared against records of app usage collected by day-truncated local date at a time of capture.
     * It might not be precise enough for all applications.
     */
    suspend fun getNumberOfDaysAppUsedSinceDate(date: Date): Long

    /**
     * Get the last day the app was used
     */
    suspend fun getLastActiveDay(): String

    /**
     * Get the previous active day
     */
    suspend fun getPreviousActiveDay(): String?
}
