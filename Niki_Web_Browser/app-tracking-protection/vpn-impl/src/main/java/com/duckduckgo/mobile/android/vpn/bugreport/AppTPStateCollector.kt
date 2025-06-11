

package com.duckduckgo.mobile.android.vpn.bugreport

import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.mobile.android.vpn.AppTpVpnFeature
import com.duckduckgo.mobile.android.vpn.VpnFeaturesRegistry
import com.duckduckgo.mobile.android.vpn.state.VpnStateCollectorPlugin
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject
import org.json.JSONObject

@ContributesMultibinding(ActivityScope::class)
class AppTPStateCollector @Inject constructor(
    private val vpnFeaturesRegistry: VpnFeaturesRegistry,
) : VpnStateCollectorPlugin {
    override suspend fun collectVpnRelatedState(appPackageId: String?): JSONObject {
        return JSONObject().apply {
            put("enabled", vpnFeaturesRegistry.isFeatureRunning(AppTpVpnFeature.APPTP_VPN))
        }
    }

    override val collectorName: String = "appTpState"
}
