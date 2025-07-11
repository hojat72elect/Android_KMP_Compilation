

package com.duckduckgo.mobile.android.vpn.state

import org.json.JSONObject

/**
 * Implement this interface and return the multibinding to return VPN-relevant state information
 *
 * The VPN will call all the [collectVpnRelatedState] methods when it wants to send information
 * about the VPN state. This is generally done when an event happens that requires an AppTP bugreport
 * to be sent, eg. user unprotects an app or reports breakage.
 */
interface VpnStateCollectorPlugin {
    suspend fun collectVpnRelatedState(appPackageId: String? = null): JSONObject
    val collectorName: String
}
