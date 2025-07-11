

package com.duckduckgo.networkprotection.impl.integration

import com.duckduckgo.common.utils.network.isCGNATed
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.mobile.android.vpn.VpnFeaturesRegistry
import com.duckduckgo.mobile.android.vpn.state.VpnStateCollectorPlugin
import com.duckduckgo.networkprotection.impl.NetPVpnFeature
import com.duckduckgo.networkprotection.impl.configuration.WgTunnelConfig
import com.duckduckgo.networkprotection.impl.configuration.asServerDetails
import com.duckduckgo.networkprotection.impl.connectionclass.ConnectionQualityStore
import com.duckduckgo.networkprotection.impl.connectionclass.asConnectionQuality
import com.duckduckgo.networkprotection.impl.exclusion.NetPExclusionListRepository
import com.duckduckgo.networkprotection.impl.exclusion.systemapps.SystemAppsExclusionRepository
import com.duckduckgo.networkprotection.impl.settings.NetPSettingsLocalConfig
import com.duckduckgo.networkprotection.impl.settings.NetpVpnSettingsDataStore
import com.duckduckgo.networkprotection.store.NetPGeoswitchingRepository
import com.duckduckgo.subscriptions.api.Product.NetP
import com.duckduckgo.subscriptions.api.SubscriptionStatus
import com.duckduckgo.subscriptions.api.SubscriptionStatus.AUTO_RENEWABLE
import com.duckduckgo.subscriptions.api.SubscriptionStatus.GRACE_PERIOD
import com.duckduckgo.subscriptions.api.SubscriptionStatus.NOT_AUTO_RENEWABLE
import com.duckduckgo.subscriptions.api.Subscriptions
import com.squareup.anvil.annotations.ContributesMultibinding
import java.net.Inet4Address
import java.net.NetworkInterface
import javax.inject.Inject
import kotlinx.coroutines.flow.firstOrNull
import org.json.JSONObject

@ContributesMultibinding(ActivityScope::class)
class NetPStateCollector @Inject constructor(
    private val vpnFeaturesRegistry: VpnFeaturesRegistry,
    private val wgTunnelConfig: WgTunnelConfig,
    private val netPExclusionListRepository: NetPExclusionListRepository,
    private val connectionQualityStore: ConnectionQualityStore,
    private val netPSettingsLocalConfig: NetPSettingsLocalConfig,
    private val netPGeoswitchingRepository: NetPGeoswitchingRepository,
    private val systemAppsExclusionRepository: SystemAppsExclusionRepository,
    private val netpVpnSettingsDataStore: NetpVpnSettingsDataStore,
    private val subscriptions: Subscriptions,
) : VpnStateCollectorPlugin {

    override suspend fun collectVpnRelatedState(appPackageId: String?): JSONObject {
        val isNetpRunning = vpnFeaturesRegistry.isFeatureRunning(NetPVpnFeature.NETP_VPN)
        return JSONObject().apply {
            put("enabled", isNetpRunning)
            put("CGNATed", isCGNATed())
            JSONObject().apply {
                val active = subscriptions.getSubscriptionStatus().isActive()
                put("isActive", active)
                val vpnEntitlement = kotlin.runCatching {
                    subscriptions.getEntitlementStatus()
                        .firstOrNull()?.contains(NetP) ?: false
                }.getOrElse { false }
                put("hasVpnEntitlement", vpnEntitlement)
            }.also {
                put("subscriptionState", it)
            }

            appPackageId?.let {
                put("reportedAppProtected", !netPExclusionListRepository.getExcludedAppPackages().contains(it))
            }
            put("excludeLocalNetworks", netPSettingsLocalConfig.vpnExcludeLocalNetworkRoutes().isEnabled())
            put("excludedSystemAppCategories", systemAppsExclusionRepository.getExcludedCategories().map { it.name })
            put("pauseDuringWifiCallsEnabled", netPSettingsLocalConfig.vpnPauseDuringCalls().isEnabled())
            put("customDNS", !netpVpnSettingsDataStore.customDns.isNullOrEmpty())
            put("autoExcludeBrokenApps", netPSettingsLocalConfig.autoExcludeBrokenApps().isEnabled())
            if (isNetpRunning) {
                val serverDetails = wgTunnelConfig.getWgConfig()?.asServerDetails()
                put("connectedServer", serverDetails?.location ?: "Unknown")
                put("connectedServerIP", serverDetails?.ipAddress ?: "Unknown")
                put("connectionQuality", connectionQualityStore.getConnectionLatency().asConnectionQuality())
                put("customServerSelection", netPGeoswitchingRepository.getUserPreferredLocation().countryCode != null)
            }
        }
    }

    override val collectorName: String = "netPState"

    private fun isCGNATed(): Boolean {
        NetworkInterface.getNetworkInterfaces().asSequence().filter { !it.name.contains("tun") }.map { it.interfaceAddresses }.forEach { addrs ->
            addrs.filter { it.address is Inet4Address && !it.address.isLoopbackAddress }.forEach { interfaceAddress ->
                if (interfaceAddress.address.isCGNATed()) {
                    return true
                }
            }
        }

        return false
    }

    private fun SubscriptionStatus.isActive(): Boolean {
        return when (this) {
            AUTO_RENEWABLE, NOT_AUTO_RENEWABLE, GRACE_PERIOD -> true
            else -> false
        }
    }
}
