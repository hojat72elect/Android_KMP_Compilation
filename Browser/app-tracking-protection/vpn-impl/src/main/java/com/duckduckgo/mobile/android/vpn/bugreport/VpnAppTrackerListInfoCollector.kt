

package com.duckduckgo.mobile.android.vpn.bugreport

import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.mobile.android.vpn.state.VpnStateCollectorPlugin
import com.duckduckgo.mobile.android.vpn.store.VpnDatabase
import com.duckduckgo.mobile.android.vpn.trackers.AppTrackerRepository
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject
import org.json.JSONObject

@ContributesMultibinding(ActivityScope::class)
class VpnAppTrackerListInfoCollector @Inject constructor(
    private val vpnDatabase: VpnDatabase,
    private val appTrackerRepository: AppTrackerRepository,
) : VpnStateCollectorPlugin {

    override val collectorName: String
        get() = "vpnTrackerLists"

    override suspend fun collectVpnRelatedState(appPackageId: String?): JSONObject {
        return JSONObject().apply {
            put(APP_TRACKER_BLOCKLIST, vpnDatabase.vpnAppTrackerBlockingDao().getTrackerBlocklistMetadata()?.eTag.orEmpty())
            put(APP_EXCLUSION_LIST, vpnDatabase.vpnAppTrackerBlockingDao().getExclusionListMetadata()?.eTag.orEmpty())
            put(APP_EXCEPTION_RULE_LIST, vpnDatabase.vpnAppTrackerBlockingDao().getTrackerExceptionRulesMetadata()?.eTag.orEmpty())
            appPackageId?.let {
                put(PACKAGE_ID_IS_PROTECTED, isUnprotectedByDefault(appPackageId).toString())
                put(PACKAGE_ID_PROTECTION_OVERRIDEN, isProtectionOverriden(appPackageId).toString())
            }
        }
    }

    private fun isUnprotectedByDefault(appPackageId: String): Boolean {
        return appTrackerRepository.getAppExclusionList().any { it.packageId == appPackageId }
    }

    private fun isProtectionOverriden(appPackageId: String): Boolean {
        return appTrackerRepository.getManualAppExclusionList().firstOrNull { it.packageId == appPackageId } != null
    }

    companion object {
        private const val APP_TRACKER_BLOCKLIST = "appTrackerListEtag"
        private const val APP_EXCLUSION_LIST = "appExclusionListEtag"
        private const val APP_EXCEPTION_RULE_LIST = "appExceptionRuleListEtag"
        private const val PACKAGE_ID_IS_PROTECTED = "reportedAppUnprotectedByDefault"
        private const val PACKAGE_ID_PROTECTION_OVERRIDEN = "overridenDefaultProtection"
    }
}
