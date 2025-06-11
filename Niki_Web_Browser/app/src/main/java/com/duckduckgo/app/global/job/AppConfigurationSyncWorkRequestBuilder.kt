

package com.duckduckgo.app.global.job

import android.content.Context
import androidx.work.*
import com.duckduckgo.anvil.annotations.ContributesWorker
import com.duckduckgo.app.job.ConfigurationDownloader
import com.duckduckgo.di.scopes.AppScope
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import timber.log.Timber

class AppConfigurationSyncWorkRequestBuilder @Inject constructor() {

    fun appConfigurationWork(): PeriodicWorkRequest {
        return PeriodicWorkRequestBuilder<AppConfigurationWorker>(12, TimeUnit.HOURS)
            .setConstraints(networkAvailable())
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 60, TimeUnit.MINUTES)
            .addTag(APP_CONFIG_SYNC_WORK_TAG)
            .build()
    }

    private fun networkAvailable() = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

    companion object {
        const val APP_CONFIG_SYNC_WORK_TAG = "AppConfigurationWorker"
    }
}

@ContributesWorker(AppScope::class)
class AppConfigurationWorker(
    context: Context,
    workerParams: WorkerParameters,
) : RxWorker(context, workerParams) {

    @Inject
    lateinit var appConfigurationDownloader: ConfigurationDownloader

    override fun createWork(): Single<Result> {
        Timber.i("Running app config sync")
        return appConfigurationDownloader.downloadTask()
            .toSingle {
                Timber.i("App configuration sync was successful")
                Result.success()
            }
            .onErrorReturn {
                Timber.w(it, "App configuration sync work failed")
                Result.retry()
            }
    }
}
