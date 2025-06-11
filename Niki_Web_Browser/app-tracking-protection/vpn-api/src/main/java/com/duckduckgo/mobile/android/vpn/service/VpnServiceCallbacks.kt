

package com.duckduckgo.mobile.android.vpn.service

import com.duckduckgo.mobile.android.vpn.state.VpnStateMonitor.VpnStopReason
import kotlinx.coroutines.CoroutineScope

interface VpnServiceCallbacks {
    fun onVpnStarting(coroutineScope: CoroutineScope) {}

    fun onVpnStartFailed(coroutineScope: CoroutineScope) {}

    fun onVpnStarted(coroutineScope: CoroutineScope)

    fun onVpnReconfigured(coroutineScope: CoroutineScope) {}

    fun onVpnStopped(
        coroutineScope: CoroutineScope,
        vpnStopReason: VpnStopReason,
    )
}
