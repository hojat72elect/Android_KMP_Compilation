

package com.duckduckgo.app.statistics.api

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.work.*
import com.duckduckgo.anvil.annotations.ContributesWorker
import com.duckduckgo.app.lifecycle.MainProcessLifecycleObserver
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import timber.log.Timber

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = MainProcessLifecycleObserver::class,
)
class OfflinePixelScheduler @Inject constructor(
    private val workManager: WorkManager,
) : MainProcessLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        scheduleOfflinePixels()
    }

    private fun scheduleOfflinePixels() {
        Timber.v("Scheduling offline pixels to be sent")

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = PeriodicWorkRequestBuilder<OfflinePixelWorker>(SERVICE_INTERVAL, SERVICE_TIME_UNIT)
            .addTag(WORK_REQUEST_TAG)
            .setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, BACKOFF_INTERVAL, BACKOFF_TIME_UNIT)
            .build()

        workManager.enqueueUniquePeriodicWork(WORK_REQUEST_TAG, ExistingPeriodicWorkPolicy.KEEP, request)
    }

    companion object {
        private const val WORK_REQUEST_TAG = "com.duckduckgo.statistics.offlinepixels.schedule"
        private const val SERVICE_INTERVAL = 3L
        private val SERVICE_TIME_UNIT = TimeUnit.HOURS
        private const val BACKOFF_INTERVAL = 10L
        private val BACKOFF_TIME_UNIT = TimeUnit.MINUTES
    }
}

@ContributesWorker(AppScope::class)
open class OfflinePixelWorker(
    val context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {
    @Inject
    lateinit var offlinePixelSender: OfflinePixelSender
    override suspend fun doWork(): Result {
        return try {
            offlinePixelSender
                .sendOfflinePixels()
                .blockingAwait()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
