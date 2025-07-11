

package com.duckduckgo.networkprotection.impl.pixels

import android.content.Context
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.common.utils.extensions.getPrivateDnsServerName
import com.duckduckgo.common.utils.extensions.isPrivateDnsActive
import com.duckduckgo.di.scopes.VpnScope
import com.duckduckgo.mobile.android.vpn.service.VpnServiceCallbacks
import com.duckduckgo.mobile.android.vpn.state.VpnStateMonitor.VpnStopReason
import com.duckduckgo.networkprotection.api.NetworkProtectionState
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@ContributesMultibinding(VpnScope::class)
class PrivateDnsSettingReporter @Inject constructor(
    private val networkProtectionPixels: NetworkProtectionPixels,
    private val dispatcherProvider: DispatcherProvider,
    private val networkProtectionState: NetworkProtectionState,
    private val context: Context,
) : VpnServiceCallbacks {
    override fun onVpnStarted(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            reportPrivateDns(false)
        }
    }

    override fun onVpnReconfigured(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            reportPrivateDns(false)
        }
    }

    override fun onVpnStartFailed(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            reportPrivateDns(true)
        }
    }

    override fun onVpnStopped(coroutineScope: CoroutineScope, vpnStopReason: VpnStopReason) {
        // noop
    }

    private suspend fun reportPrivateDns(failedStart: Boolean) = withContext(dispatcherProvider.io()) {
        if (networkProtectionState.isEnabled()) {
            // We are interested (for now) in users that manually set the Private DNS
            // When private DNS is in auto mode, the value will be "unknown"
            val default = if (context.isPrivateDnsActive()) "unknown" else ""
            context.getPrivateDnsServerName()?.let { dns ->
                if (dns != default) {
                    if (failedStart) {
                        networkProtectionPixels.reportPrivateDnsSetOnVpnStartFail()
                    } else {
                        networkProtectionPixels.reportPrivateDnsSet()
                    }
                }
            }
        }
    }
}
