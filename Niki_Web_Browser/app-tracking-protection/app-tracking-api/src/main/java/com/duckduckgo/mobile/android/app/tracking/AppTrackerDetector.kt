

package com.duckduckgo.mobile.android.app.tracking

import androidx.annotation.WorkerThread

interface AppTrackerDetector {
    /**
     * Evaluates whether the specified domain requested by the specified uid is a tracker.
     * This method should be called off the UI thread.
     *
     * @param domain the domain to evaluate
     * @param uid the uid of the app requesting the domain
     *
     * @return [AppTracker] if the request is a tracker, null otherwise
     */
    @WorkerThread
    fun evaluate(domain: String, uid: Int): AppTracker?

    data class AppTracker(
        val domain: String,
        val uid: Int,
        val trackerCompanyDisplayName: String,
        val trackingAppId: String,
        val trackingAppName: String,
    )
}
