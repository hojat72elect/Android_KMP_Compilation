

package com.duckduckgo.mobile.android.vpn.bugreport

import android.content.Context
import com.duckduckgo.common.utils.extensions.getPrivateDnsServerName
import com.duckduckgo.common.utils.extensions.isPrivateDnsActive
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.mobile.android.vpn.service.TrackerBlockingVpnService
import com.duckduckgo.mobile.android.vpn.state.VpnStateCollectorPlugin
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject
import org.json.JSONObject

@ContributesMultibinding(ActivityScope::class)
class VpnServiceStateCollector @Inject constructor(
    private val context: Context,
) : VpnStateCollectorPlugin {

    override val collectorName: String = "vpnServiceState"

    override suspend fun collectVpnRelatedState(appPackageId: String?): JSONObject {
        return JSONObject().apply {
            // VPN on/off state
            put("enabled", TrackerBlockingVpnService.isServiceRunning(context).toString())
            put("privateDns", runCatching { getPrivateDnsServerName() }.getOrDefault(""))
        }
    }

    private fun getPrivateDnsServerName(): String {
        // return "unknown" when private DNS is enabled but we can't get the server name, otherwise ""
        val default = if (context.isPrivateDnsActive()) "unknown" else ""
        return context.getPrivateDnsServerName() ?: default
    }
}
