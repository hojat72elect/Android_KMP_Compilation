

package com.duckduckgo.mobile.android.vpn.connectivity

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.di.scopes.VpnScope
import com.duckduckgo.mobile.android.vpn.service.connectivity.VpnConnectivityLossListenerPlugin

@ContributesPluginPoint(
    scope = VpnScope::class,
    boundType = VpnConnectivityLossListenerPlugin::class,
)
interface VpnConnectivityLossListenerPluginPoint
