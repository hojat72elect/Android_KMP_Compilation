

package com.duckduckgo.mobile.android.vpn.bugreport

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.common.utils.plugins.PluginPoint
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.mobile.android.vpn.state.VpnStateCollector
import com.duckduckgo.mobile.android.vpn.state.VpnStateCollectorPlugin
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject
import kotlinx.coroutines.withContext
import logcat.logcat
import org.json.JSONObject

@ContributesBinding(ActivityScope::class)
@SingleInstanceIn(ActivityScope::class)
class RealVpnStateCollector @Inject constructor(
    private val vpnStateCollectors: PluginPoint<VpnStateCollectorPlugin>,
    private val dispatcherProvider: DispatcherProvider,
) : VpnStateCollector {

    override suspend fun collectVpnState(appPackageId: String?): JSONObject {
        return withContext(dispatcherProvider.io()) {
            val vpnState = JSONObject()
            // other VPN metrics
            vpnStateCollectors.getPlugins().forEach {
                logcat { "collectVpnState from ${it.collectorName}" }
                vpnState.put(it.collectorName, it.collectVpnRelatedState(appPackageId))
            }

            return@withContext vpnState
        }
    }
}

@ContributesPluginPoint(
    scope = ActivityScope::class,
    boundType = VpnStateCollectorPlugin::class,
)
@Suppress("unused")
interface VpnStateCollectorPluginPoint
