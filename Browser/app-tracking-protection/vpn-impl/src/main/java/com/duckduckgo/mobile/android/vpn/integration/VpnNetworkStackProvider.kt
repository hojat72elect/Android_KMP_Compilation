

package com.duckduckgo.mobile.android.vpn.integration

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.common.utils.plugins.PluginPoint
import com.duckduckgo.di.scopes.VpnScope
import com.duckduckgo.mobile.android.vpn.AppTpVpnFeature
import com.duckduckgo.mobile.android.vpn.VpnFeaturesRegistry
import com.duckduckgo.mobile.android.vpn.network.VpnNetworkStack
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(VpnScope::class)
class VpnNetworkStackProviderImpl @Inject constructor(
    private val vpnNetworkStacks: PluginPoint<VpnNetworkStack>,
    private val vpnFeaturesRegistry: VpnFeaturesRegistry,
) : VpnNetworkStackProvider {
    override suspend fun provideNetworkStack(): VpnNetworkStack {
        val features = vpnFeaturesRegistry.getRegisteredFeatures()
        val feature = features.firstOrNull { it.featureName == AppTpVpnFeature.APPTP_VPN.featureName }

        return vpnNetworkStacks.getPlugins().firstOrNull { it.name == feature?.featureName }
            ?: VpnNetworkStack.EmptyVpnNetworkStack
    }
}

@ContributesPluginPoint(
    scope = VpnScope::class,
    boundType = VpnNetworkStack::class,
)
interface VpnNetworkStackPluginPoint
