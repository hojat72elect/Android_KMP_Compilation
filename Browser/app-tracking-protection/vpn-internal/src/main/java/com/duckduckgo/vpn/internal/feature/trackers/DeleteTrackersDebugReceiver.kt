

package com.duckduckgo.vpn.internal.feature.trackers

import android.content.Context
import android.content.Intent
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.VpnScope
import com.duckduckgo.mobile.android.vpn.service.VpnServiceCallbacks
import com.duckduckgo.mobile.android.vpn.state.VpnStateMonitor.VpnStopReason
import com.duckduckgo.mobile.android.vpn.stats.AppTrackerBlockingStatsRepository
import com.duckduckgo.vpn.internal.feature.InternalFeatureReceiver
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import logcat.logcat

/**
 * This receiver allows deletion of previously seen trackers.
 *
 * $ adb shell am broadcast -a delete-trackers
 *
 */
class DeleteTrackersDebugReceiver(
    context: Context,
    receiver: (Intent) -> Unit,
) : InternalFeatureReceiver(context, receiver) {

    override fun intentAction(): String = ACTION

    companion object {
        private const val ACTION = "delete-trackers"

        fun createIntent(): Intent = Intent(ACTION)
    }
}

@ContributesMultibinding(VpnScope::class)
class DeleteTrackersDebugReceiverRegister @Inject constructor(
    private val context: Context,
    private val appTrackerBlockingRepository: AppTrackerBlockingStatsRepository,
    @AppCoroutineScope private val appCoroutineScope: CoroutineScope,
    private val dispatcherProvider: DispatcherProvider,
) : VpnServiceCallbacks {
    private val className: String
        get() = DeleteTrackersDebugReceiver::class.java.simpleName

    private var receiver: DeleteTrackersDebugReceiver? = null

    override fun onVpnStarted(coroutineScope: CoroutineScope) {
        logcat { "Debug receiver $className registered" }

        receiver?.unregister()

        receiver = DeleteTrackersDebugReceiver(context) {
            appCoroutineScope.launch(dispatcherProvider.io()) {
                appTrackerBlockingRepository.deleteAllTrackers()
            }
        }.apply { register() }
    }

    override fun onVpnStopped(
        coroutineScope: CoroutineScope,
        vpnStopReason: VpnStopReason,
    ) {
        receiver?.unregister()
    }
}
