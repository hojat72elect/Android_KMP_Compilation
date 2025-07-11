

package com.duckduckgo.privacy.config.impl.workers

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.work.BackoffPolicy
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.duckduckgo.anvil.annotations.ContributesWorker
import com.duckduckgo.app.lifecycle.MainProcessLifecycleObserver
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacy.config.impl.PrivacyConfigDownloader
import com.duckduckgo.privacy.config.impl.PrivacyConfigDownloader.ConfigDownloadResult.Success
import com.squareup.anvil.annotations.ContributesMultibinding
import dagger.SingleInstanceIn
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.coroutines.withContext
import timber.log.Timber

@ContributesWorker(AppScope::class)
class PrivacyConfigDownloadWorker(
    context: Context,
    workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters) {
    @Inject
    lateinit var privacyConfigDownloader: PrivacyConfigDownloader

    @Inject
    lateinit var dispatcherProvider: DispatcherProvider

    override suspend fun doWork(): Result {
        return withContext(dispatcherProvider.io()) {
            val result = privacyConfigDownloader.download()
            return@withContext if (result is Success) {
                Result.success()
            } else {
                Result.retry()
            }
        }
    }
}

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = MainProcessLifecycleObserver::class,
)
@SingleInstanceIn(AppScope::class)
class PrivacyConfigDownloadWorkerScheduler @Inject constructor(
    private val workManager: WorkManager,
) : MainProcessLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        Timber.v("Scheduling remote config worker")
        val workerRequest = PeriodicWorkRequestBuilder<PrivacyConfigDownloadWorker>(1, TimeUnit.HOURS)
            .addTag(PRIVACY_CONFIG_DOWNLOADER_WORKER_TAG)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.MINUTES)
            .build()
        workManager.enqueueUniquePeriodicWork(PRIVACY_CONFIG_DOWNLOADER_WORKER_TAG, ExistingPeriodicWorkPolicy.REPLACE, workerRequest)
    }

    companion object {
        private const val PRIVACY_CONFIG_DOWNLOADER_WORKER_TAG = "PRIVACY_CONFIG_DOWNLOADER_WORKER_TAG"
    }
}
