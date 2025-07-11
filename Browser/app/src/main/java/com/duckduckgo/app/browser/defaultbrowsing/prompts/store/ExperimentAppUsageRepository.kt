

package com.duckduckgo.app.browser.defaultbrowsing.prompts.store

import com.duckduckgo.feature.toggles.api.Toggle
import com.duckduckgo.feature.toggles.api.Toggle.State.Cohort
import java.time.format.DateTimeParseException

/**
 * Tracks the days app is used to leverage for purposes related to experiments run with the [NA Experiment Framework](https://app.asana.com/0/1208889145294658/1208889101183474).
 *
 * See [Cohort.enrollmentDateET] for how the framework stores enrollment dates.
 */
interface ExperimentAppUsageRepository {

    suspend fun recordAppUsedNow()

    /**
     * Returns the number of active days the app has been used since enrollment.
     *
     * Crossing a dateline in local time will not increment the returned count.
     * Only if a given instant crossed dateline in ET timezone, the value will be incremented.
     *
     * @return Count if successful.
     *  [UserNotEnrolledException] if the given feature is disable or user is not assigned to a cohort.
     *  [DateTimeParseException] if the enrollment date is malformed.
     */
    suspend fun getActiveDaysUsedSinceEnrollment(toggle: Toggle): Result<Long>

    class UserNotEnrolledException : Exception()
}
