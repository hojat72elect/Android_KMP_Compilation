

package com.duckduckgo.networkprotection.impl.pixels

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
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.mobile.android.vpn.VpnFeaturesRegistry
import com.duckduckgo.networkprotection.impl.NetPVpnFeature
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import logcat.logcat

@Module
@ContributesTo(AppScope::class)
object NetworkProtectionStatusReportingModule {
    @Provides
    @IntoSet
    fun provideNetworkProtectionStatusReporting(workManager: WorkManager): MainProcessLifecycleObserver {
        return NetworkProtectionStatusReporting(workManager)
    }
}

class NetworkProtectionStatusReporting(
    private val workManager: WorkManager,
) : MainProcessLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        scheduleNetworkProtectionStatusReporting()
    }

    private fun scheduleNetworkProtectionStatusReporting() {
        logcat { "Scheduling the NetworkProtectionStatusReporting worker" }

        PeriodicWorkRequestBuilder<NetworkProtectionStatusReportingWorker>(2, TimeUnit.HOURS)
            .addTag(WORKER_STATUS_REPORTING_TAG_V2)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.MINUTES)
            .build().run { workManager.enqueueUniquePeriodicWork(WORKER_STATUS_REPORTING_TAG_V2, ExistingPeriodicWorkPolicy.KEEP, this) }
    }

    companion object {
        private const val WORKER_STATUS_REPORTING_TAG_V2 = "WORKER_STATUS_REPORTING_TAG_V2"
    }
}

@ContributesWorker(AppScope::class)
class NetworkProtectionStatusReportingWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {
    @Inject
    lateinit var netpPixels: NetworkProtectionPixels

    @Inject
    lateinit var vpnFeaturesRegistry: VpnFeaturesRegistry

    override suspend fun doWork(): Result {
        if (vpnFeaturesRegistry.isFeatureRegistered(NetPVpnFeature.NETP_VPN)) {
            netpPixels.reportEnabled()
        } else {
            netpPixels.reportDisabled()
        }

        return Result.success()
    }
}
