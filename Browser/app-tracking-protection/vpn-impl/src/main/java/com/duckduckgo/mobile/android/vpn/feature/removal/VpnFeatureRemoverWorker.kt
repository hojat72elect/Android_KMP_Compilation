

package com.duckduckgo.mobile.android.vpn.feature.removal

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.duckduckgo.anvil.annotations.ContributesWorker
import com.duckduckgo.di.scopes.AppScope
import javax.inject.Inject
import logcat.logcat

@ContributesWorker(AppScope::class)
class VpnFeatureRemoverWorker(
    val context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {

    @Inject
    lateinit var vpnFeatureRemover: VpnFeatureRemover

    override suspend fun doWork(): Result {
        logcat { "VpnFeatureRemoverWorker, automatically removing AppTP feature" }
        vpnFeatureRemover.scheduledRemoveFeature()
        return Result.success()
    }

    companion object {
        const val WORKER_VPN_FEATURE_REMOVER_TAG = "VpnFeatureRemoverTagWorker"
    }
}
