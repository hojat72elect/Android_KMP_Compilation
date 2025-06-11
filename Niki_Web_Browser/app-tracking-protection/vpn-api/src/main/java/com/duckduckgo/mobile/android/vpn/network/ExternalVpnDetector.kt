

package com.duckduckgo.mobile.android.vpn.network

interface ExternalVpnDetector {

    /**
     * Detects if the current network connection is using an external VPN
     *
     * @return returns a boolean that identifies the connection being routed through a VPN
     */
    suspend fun isExternalVpnDetected(): Boolean
}
