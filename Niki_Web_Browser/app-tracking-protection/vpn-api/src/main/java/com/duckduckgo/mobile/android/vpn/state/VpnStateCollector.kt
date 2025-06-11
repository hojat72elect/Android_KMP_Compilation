

package com.duckduckgo.mobile.android.vpn.state

import org.json.JSONObject

interface VpnStateCollector {
    /**
     * Collects information about the VPN internal state
     *
     * @return returns a map that contains the information that relates to VPN state
     */
    suspend fun collectVpnState(appPackageId: String? = null): JSONObject
}
