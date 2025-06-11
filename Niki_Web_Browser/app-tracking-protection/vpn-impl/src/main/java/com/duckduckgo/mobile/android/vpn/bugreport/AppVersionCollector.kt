

package com.duckduckgo.mobile.android.vpn.bugreport

import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.mobile.android.vpn.state.VpnStateCollectorPlugin
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject
import org.json.JSONObject

@ContributesMultibinding(ActivityScope::class)
class AppVersionCollector @Inject constructor(
    private val appBuildConfig: AppBuildConfig,
) : VpnStateCollectorPlugin {
    override val collectorName: String
        get() = "appInfo"

    override suspend fun collectVpnRelatedState(appPackageId: String?): JSONObject {
        return JSONObject().apply {
            put("appVersion", appBuildConfig.versionName)
        }
    }
}
