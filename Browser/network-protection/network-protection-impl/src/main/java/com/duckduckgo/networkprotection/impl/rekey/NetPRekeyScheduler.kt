

package com.duckduckgo.networkprotection.impl.rekey

import com.duckduckgo.common.utils.ConflatedJob
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.VpnScope
import com.duckduckgo.mobile.android.vpn.VpnFeaturesRegistry
import com.duckduckgo.mobile.android.vpn.service.VpnServiceCallbacks
import com.duckduckgo.mobile.android.vpn.state.VpnStateMonitor.VpnStopReason
import com.duckduckgo.networkprotection.impl.NetPVpnFeature
import com.squareup.anvil.annotations.ContributesMultibinding
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.coroutines.*
import logcat.logcat

@ContributesMultibinding(VpnScope::class)
class NetPRekeyScheduler @Inject constructor(
    private val vpnFeaturesRegistry: VpnFeaturesRegistry,
    private val dispatcherProvider: DispatcherProvider,
    private val netPRekeyer: NetPRekeyer,
) : VpnServiceCallbacks {

    private val job = ConflatedJob()

    override fun onVpnStarted(coroutineScope: CoroutineScope) {
        job += coroutineScope.launch(dispatcherProvider.io()) {
            while (isActive && vpnFeaturesRegistry.isFeatureRegistered(NetPVpnFeature.NETP_VPN)) {
                logcat { "Start periodic re-keying attempts" }
                delay(TimeUnit.HOURS.toMillis(1)) // try to re-key every 1h

                netPRekeyer.doRekey()
            }
        }
    }

    override fun onVpnReconfigured(coroutineScope: CoroutineScope) {
        // When we reconfigure the VPN we also need to make sure we schedule the re-key process
        onVpnStarted(coroutineScope)
    }

    override fun onVpnStopped(
        coroutineScope: CoroutineScope,
        vpnStopReason: VpnStopReason,
    ) {
        job.cancel()
    }
}
