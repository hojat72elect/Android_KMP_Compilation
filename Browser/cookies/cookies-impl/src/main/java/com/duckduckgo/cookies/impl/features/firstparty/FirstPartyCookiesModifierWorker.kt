

package com.duckduckgo.cookies.impl.features.firstparty

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
import com.duckduckgo.cookies.api.CookiesFeatureName
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.FeatureToggle
import com.squareup.anvil.annotations.ContributesMultibinding
import dagger.SingleInstanceIn
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.DAYS
import javax.inject.Inject
import kotlinx.coroutines.withContext

@ContributesWorker(AppScope::class)
class FirstPartyCookiesModifierWorker(
    context: Context,
    workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters) {
    @Inject
    lateinit var firstPartyCookiesModifier: FirstPartyCookiesModifier

    @Inject
    lateinit var dispatcherProvider: DispatcherProvider

    override suspend fun doWork(): Result {
        return withContext(dispatcherProvider.io()) {
            firstPartyCookiesModifier.expireFirstPartyCookies()
            Result.success()
        }
    }
}

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = MainProcessLifecycleObserver::class,
)
@SingleInstanceIn(AppScope::class)
class FirstPartyCookiesModifierWorkerScheduler @Inject constructor(
    private val workManager: WorkManager,
    private val toggle: FeatureToggle,
) : MainProcessLifecycleObserver {

    private val workerRequest = PeriodicWorkRequestBuilder<FirstPartyCookiesModifierWorker>(1, DAYS)
        .addTag(FIRST_PARTY_COOKIES_EXPIRE_WORKER_TAG)
        .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.MINUTES)
        .build()

    override fun onStop(owner: LifecycleOwner) {
        if (isFeatureEnabled()) {
            workManager.enqueueUniquePeriodicWork(FIRST_PARTY_COOKIES_EXPIRE_WORKER_TAG, ExistingPeriodicWorkPolicy.REPLACE, workerRequest)
        } else {
            workManager.cancelAllWorkByTag(FIRST_PARTY_COOKIES_EXPIRE_WORKER_TAG)
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        if (isFeatureEnabled()) {
            workManager.enqueueUniquePeriodicWork(FIRST_PARTY_COOKIES_EXPIRE_WORKER_TAG, ExistingPeriodicWorkPolicy.KEEP, workerRequest)
        } else {
            workManager.cancelAllWorkByTag(FIRST_PARTY_COOKIES_EXPIRE_WORKER_TAG)
        }
    }

    private fun isFeatureEnabled(): Boolean = toggle.isFeatureEnabled(CookiesFeatureName.Cookie.value)

    companion object {
        private const val FIRST_PARTY_COOKIES_EXPIRE_WORKER_TAG = "FIRST_PARTY_COOKIES_EXPIRE_WORKER_TAG"
    }
}
