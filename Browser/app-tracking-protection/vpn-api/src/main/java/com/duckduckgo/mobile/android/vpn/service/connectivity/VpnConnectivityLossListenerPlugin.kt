

package com.duckduckgo.mobile.android.vpn.service.connectivity

import kotlinx.coroutines.CoroutineScope

interface VpnConnectivityLossListenerPlugin {
    /**
     * This method will be called whenever we detect a VPN connectivity loss while the device actually has connectivity.
     */
    fun onVpnConnectivityLoss(coroutineScope: CoroutineScope) {}

    /**
     * This method will be called whenever both the VPN and Device have connectivity.
     * Be aware that this would be called periodically, regardless if the VPN connectivity loss was detected prior or not.
     */
    fun onVpnConnected(coroutineScope: CoroutineScope) {}
}
