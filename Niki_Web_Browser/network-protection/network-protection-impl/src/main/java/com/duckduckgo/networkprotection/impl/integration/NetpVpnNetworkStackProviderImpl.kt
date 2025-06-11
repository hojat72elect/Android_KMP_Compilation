

package com.duckduckgo.networkprotection.impl.integration

import com.duckduckgo.common.utils.plugins.PluginPoint
import com.duckduckgo.di.scopes.VpnScope
import com.duckduckgo.mobile.android.vpn.VpnFeaturesRegistry
import com.duckduckgo.mobile.android.vpn.integration.VpnNetworkStackProvider
import com.duckduckgo.mobile.android.vpn.network.VpnNetworkStack
import com.duckduckgo.networkprotection.impl.NetPVpnFeature
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(
    scope = VpnScope::class,
    rank = ContributesBinding.RANK_HIGHEST, // we replace the provider in AppTP using DI without having to touch production code
)
class NetpVpnNetworkStackProviderImpl @Inject constructor(
    private val vpnNetworkStacks: PluginPoint<VpnNetworkStack>,
    private val vpnFeaturesRegistry: VpnFeaturesRegistry,
) : VpnNetworkStackProvider {
    override suspend fun provideNetworkStack(): VpnNetworkStack {
        val features = vpnFeaturesRegistry.getRegisteredFeatures().map { it.featureName }

        val networkStack = if (features.contains(NetPVpnFeature.NETP_VPN.featureName)) {
            // if we have NetP enabled, return NetP's network stack
            vpnNetworkStacks.getPlugins().firstOrNull { it.name == NetPVpnFeature.NETP_VPN.featureName }
        } else {
            // else we return whatever the other network is
            // TODO once we go to production there will be only ONE provider so this will be removed
            vpnNetworkStacks.getPlugins().firstOrNull { it.name != NetPVpnFeature.NETP_VPN.featureName }
        }

        return networkStack ?: VpnNetworkStack.EmptyVpnNetworkStack
    }
}
