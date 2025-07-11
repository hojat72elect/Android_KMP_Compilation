

package com.duckduckgo.mobile.android.vpn.pixels

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.work.*
import com.duckduckgo.anvil.annotations.ContributesWorker
import com.duckduckgo.app.lifecycle.MainProcessLifecycleObserver
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.mobile.android.vpn.AppTpVpnFeature
import com.duckduckgo.mobile.android.vpn.VpnFeaturesRegistry
import com.duckduckgo.mobile.android.vpn.dao.VpnServiceStateStatsDao
import com.duckduckgo.mobile.android.vpn.store.VpnDatabase
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import logcat.logcat

@Module
@ContributesTo(AppScope::class)
object DeviceShieldStatusReportingModule {
    @Provides
    @IntoSet
    fun provideDeviceShieldStatusReporting(workManager: WorkManager): MainProcessLifecycleObserver {
        return DeviceShieldStatusReporting(workManager)
    }

    @Provides
    fun provideVpnServiceStateStatsDao(vpnDatabase: VpnDatabase): VpnServiceStateStatsDao {
        return vpnDatabase.vpnServiceStateDao()
    }
}

class DeviceShieldStatusReporting(
    private val workManager: WorkManager,
) : MainProcessLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        scheduleDeviceShieldStatusReporting()
    }

    private fun scheduleDeviceShieldStatusReporting() {
        logcat { "Scheduling the DeviceShieldStatusReporting worker" }

        PeriodicWorkRequestBuilder<DeviceShieldStatusReportingWorker>(24, TimeUnit.HOURS)
            .addTag(WORKER_STATUS_REPORTING_TAG)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.MINUTES)
            .build().run { workManager.enqueueUniquePeriodicWork(WORKER_STATUS_REPORTING_TAG, ExistingPeriodicWorkPolicy.KEEP, this) }
    }

    companion object {
        private const val WORKER_STATUS_REPORTING_TAG = "WORKER_STATUS_REPORTING_TAG"
    }
}

@ContributesWorker(AppScope::class)
class DeviceShieldStatusReportingWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {
    @Inject
    lateinit var deviceShieldPixels: DeviceShieldPixels

    @Inject
    lateinit var vpnFeaturesRegistry: VpnFeaturesRegistry

    override suspend fun doWork(): Result {
        if (vpnFeaturesRegistry.isFeatureRegistered(AppTpVpnFeature.APPTP_VPN)) {
            deviceShieldPixels.reportEnabled()
        } else {
            deviceShieldPixels.reportDisabled()
        }

        return Result.success()
    }
}
