

package com.duckduckgo.remote.messaging.impl

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.work.*
import com.duckduckgo.anvil.annotations.ContributesWorker
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.lifecycle.MainProcessLifecycleObserver
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacy.config.api.PrivacyConfigCallbackPlugin
import com.duckduckgo.remote.messaging.store.RemoteMessagingConfigRepository
import com.squareup.anvil.annotations.ContributesMultibinding
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@ContributesWorker(AppScope::class)
class RemoteMessagingConfigDownloadWorker(
    context: Context,
    workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters) {
    @Inject
    lateinit var downloader: RemoteMessagingConfigDownloader

    @Inject
    lateinit var dispatcherProvider: DispatcherProvider

    override suspend fun doWork(): Result {
        return withContext(dispatcherProvider.io()) {
            val result = downloader.download()
            return@withContext if (result) {
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

@ContributesMultibinding(
    AppScope::class,
    boundType = PrivacyConfigCallbackPlugin::class,
)
class RemoteMessagingConfigDownloadScheduler @Inject constructor(
    private val workManager: WorkManager,
    private val downloader: RemoteMessagingConfigDownloader,
    @AppCoroutineScope private val appCoroutineScope: CoroutineScope,
    private val dispatcherProvider: DispatcherProvider,
    private val remoteMessagingConfigRepository: RemoteMessagingConfigRepository,
    private val remoteMessagingFeatureToggles: RemoteMessagingFeatureToggles,
) : MainProcessLifecycleObserver, PrivacyConfigCallbackPlugin {

    override fun onCreate(owner: LifecycleOwner) {
        scheduleDownload()
    }

    override fun onPrivacyConfigDownloaded() {
        if (remoteMessagingFeatureToggles.invalidateRMFAfterPrivacyConfigDownloaded().isEnabled()) {
            appCoroutineScope.launch(context = dispatcherProvider.io()) {
                Timber.d("RMF: onPrivacyConfigDownloaded, invalidating and re-downloading")
                remoteMessagingConfigRepository.invalidate()
                downloader.download()
            }
        }
    }

    private fun scheduleDownload() {
        Timber.v("RMF: Scheduling remote config worker")
        val workerRequest = PeriodicWorkRequestBuilder<RemoteMessagingConfigDownloadWorker>(4, TimeUnit.HOURS)
            .addTag(REMOTE_MESSAGING_DOWNLOADER_WORKER_TAG)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.MINUTES)
            .build()
        workManager.enqueueUniquePeriodicWork(REMOTE_MESSAGING_DOWNLOADER_WORKER_TAG, ExistingPeriodicWorkPolicy.REPLACE, workerRequest)
    }

    companion object {
        private const val REMOTE_MESSAGING_DOWNLOADER_WORKER_TAG = "REMOTE_MESSAGING_DOWNLOADER_WORKER_TAG"
    }
}
