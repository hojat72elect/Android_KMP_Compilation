

package com.duckduckgo.networkprotection.impl.exclusion

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.mobile.android.vpn.VpnFeaturesRegistry
import com.duckduckgo.networkprotection.api.NetworkProtectionExclusionList
import com.duckduckgo.networkprotection.impl.NetPVpnFeature
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class RealNetworkProtectionExclusionList @Inject constructor(
    private val netPExclusionListRepository: NetPExclusionListRepository,
    private val vpnFeaturesRegistry: VpnFeaturesRegistry,
) : NetworkProtectionExclusionList {
    override suspend fun isExcluded(packageName: String): Boolean {
        return vpnFeaturesRegistry.isFeatureRegistered(NetPVpnFeature.NETP_VPN) &&
            netPExclusionListRepository.getExcludedAppPackages().contains(packageName)
    }
}
