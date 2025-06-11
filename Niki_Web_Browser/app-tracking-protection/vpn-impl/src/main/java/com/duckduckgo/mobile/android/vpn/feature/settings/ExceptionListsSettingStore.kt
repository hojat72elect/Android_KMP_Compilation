

package com.duckduckgo.mobile.android.vpn.feature.settings

import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.FeatureSettings
import com.duckduckgo.feature.toggles.api.RemoteFeatureStoreNamed
import com.duckduckgo.mobile.android.vpn.AppTpVpnFeature
import com.duckduckgo.mobile.android.vpn.VpnFeaturesRegistry
import com.duckduckgo.mobile.android.vpn.feature.AppTpRemoteFeatures
import com.duckduckgo.mobile.android.vpn.store.VpnDatabase
import com.duckduckgo.mobile.android.vpn.trackers.AppTrackerExceptionRule
import com.duckduckgo.mobile.android.vpn.trackers.AppTrackerExcludedPackage
import com.duckduckgo.mobile.android.vpn.trackers.AppTrackerSystemAppOverridePackage
import com.squareup.anvil.annotations.ContributesBinding
import com.squareup.moshi.Moshi
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import logcat.LogPriority
import logcat.asLog
import logcat.logcat

@ContributesBinding(AppScope::class)
@RemoteFeatureStoreNamed(AppTpRemoteFeatures::class)
class ExceptionListsSettingStore @Inject constructor(
    private val vpnDatabase: VpnDatabase,
    @AppCoroutineScope private val appCoroutineScope: CoroutineScope,
    private val vpnFeaturesRegistry: VpnFeaturesRegistry,
    private val dispatcherProvider: DispatcherProvider,
) : FeatureSettings.Store {
    private val jsonAdapter = Moshi.Builder().build().adapter(JsonConfigModel::class.java)

    override fun store(jsonString: String) {
        logcat { "Received configuration: $jsonString" }
        runCatching {
            jsonAdapter.fromJson(jsonString)?.exceptionLists?.let { exceptionLists ->

                val appTrackerExceptionRuleList = exceptionLists.appTrackerAllowList.map { appTrackerAllowRule ->
                    AppTrackerExceptionRule(
                        appTrackerAllowRule.domain,
                        appTrackerAllowRule.packageNames.map { it.packageName },
                    )
                }

                vpnDatabase.vpnAppTrackerBlockingDao().updateTrackerExceptionRules(appTrackerExceptionRuleList)
                vpnDatabase.vpnAppTrackerBlockingDao().updateExclusionList(exceptionLists.unprotectedApps)
                vpnDatabase.vpnSystemAppsOverridesDao().upsertSystemAppOverrides(
                    exceptionLists.unhideSystemApps.map { AppTrackerSystemAppOverridePackage(it) },
                )

                // Restart VPN now that the lists were updated
                appCoroutineScope.launch(dispatcherProvider.io()) {
                    vpnFeaturesRegistry.refreshFeature(AppTpVpnFeature.APPTP_VPN)
                }
            }
        }.onFailure {
            logcat(LogPriority.WARN) { it.asLog() }
        }
    }

    private data class JsonConfigModel(
        val exceptionLists: JsonExceptionListsModel?,
    )

    private data class JsonExceptionListsModel(
        val appTrackerAllowList: List<AppTrackerAllowRuleModel>,
        val unprotectedApps: List<AppTrackerExcludedPackage>,
        val unhideSystemApps: List<String>,
    )

    private data class AppTrackerAllowRuleModel(
        val domain: String,
        val packageNames: List<AllowedPackageModel>,
    )

    private data class AllowedPackageModel(
        val packageName: String,
    )
}
