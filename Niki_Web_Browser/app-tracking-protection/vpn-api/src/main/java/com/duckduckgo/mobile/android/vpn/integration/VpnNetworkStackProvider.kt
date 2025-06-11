

package com.duckduckgo.mobile.android.vpn.integration

import com.duckduckgo.mobile.android.vpn.network.VpnNetworkStack

/**
 * This class is used to provide the VPN network stack to the app tracking protection module.
 *
 * Note: This class is exposed in the vpn-api module just temporarily
 * TODO move this class back into the vpn-impl module
 */
interface VpnNetworkStackProvider {

    /**
     * CAll this method to get the VPN network stack to be used in the VPN service.
     */
    @Throws(IllegalStateException::class)
    suspend fun provideNetworkStack(): VpnNetworkStack
}
