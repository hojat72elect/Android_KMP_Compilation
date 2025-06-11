

package com.duckduckgo.app.job

import androidx.work.WorkManager
import com.duckduckgo.app.job.JobCleaner.Companion.allDeprecatedNotificationWorkTags
import com.duckduckgo.app.job.JobCleaner.Companion.allDeprecatedWorkerTags
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject

interface JobCleaner {
    fun cleanDeprecatedJobs()

    companion object {
        private const val STICKY_SEARCH_CONTINUOUS_APP_USE_REQUEST_TAG = "com.duckduckgo.notification.schedule.continuous"
        private const val USE_OUR_APP_WORK_REQUEST_TAG = "com.duckduckgo.notification.useOurApp"
        private const val FAVORITES_ONBOARDING_WORK_TAG = "FavoritesOnboardingWorker"
        private const val EMAIL_WAITLIST_SYNC_WORK_TAG = "EmailWaitlistWorker"
        private const val MACOS_WAITLIST_SYNC_WORK_TAG = "MacOsWaitlistWorker"
        private const val APP_TP_WAITLIST_SYNC_WORK_TAG = "AppTPWaitlistWorker"
        private const val NETP_LATENCY_MONITOR_WORKER_TAG = "NETP_LATENCY_MONITOR_WORKER_TAG"
        private const val DAILY_NETP_REKEY_TAG = "DAILY_NETP_REKEY_TAG"
        private const val WORKER_NETP_STATUS_REPORTING_TAG = "WORKER_NETP_STATUS_REPORTING_TAG"
        private const val WORKER_WAITLIST_CHDCKER_TAG = "com.duckduckgo.netp.waitlist.checker.worker"

        fun allDeprecatedNotificationWorkTags() = listOf(STICKY_SEARCH_CONTINUOUS_APP_USE_REQUEST_TAG, USE_OUR_APP_WORK_REQUEST_TAG)
        fun allDeprecatedWorkerTags() = listOf(
            FAVORITES_ONBOARDING_WORK_TAG,
            EMAIL_WAITLIST_SYNC_WORK_TAG,
            MACOS_WAITLIST_SYNC_WORK_TAG,
            APP_TP_WAITLIST_SYNC_WORK_TAG,
            NETP_LATENCY_MONITOR_WORKER_TAG,
            DAILY_NETP_REKEY_TAG,
            WORKER_NETP_STATUS_REPORTING_TAG,
            WORKER_WAITLIST_CHDCKER_TAG,
        )
    }
}

@ContributesBinding(AppScope::class)
@SingleInstanceIn(AppScope::class)
class AndroidJobCleaner @Inject constructor(private val workManager: WorkManager) : JobCleaner {

    override fun cleanDeprecatedJobs() {
        allDeprecatedNotificationWorkTags().plus(allDeprecatedWorkerTags()).forEach { tag ->
            workManager.cancelAllWorkByTag(tag)
        }
    }
}
