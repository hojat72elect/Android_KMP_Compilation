

package com.duckduckgo.vpn.internal.feature.rules

import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.mobile.android.vpn.store.VpnDatabase
import com.duckduckgo.mobile.android.vpn.trackers.AppTrackerExceptionRule
import javax.inject.Inject
import kotlinx.coroutines.withContext

class ExclusionRulesRepository @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    vpnDatabase: VpnDatabase,
) {
    private val blockingDao = vpnDatabase.vpnAppTrackerBlockingDao()

    suspend fun upsertRule(
        appPackageName: String,
        domain: String,
    ) = withContext(dispatcherProvider.io()) {
        val rule = blockingDao.getRuleByTrackerDomain(domain)
        if (rule != null) {
            val updatedRule = rule.copy(
                rule = rule.rule,
                packageNames = rule.packageNames.toMutableSet().apply { add(appPackageName) }.toList(),
            )
            blockingDao.insertTrackerExceptionRules(listOf(updatedRule))
        } else {
            blockingDao.insertTrackerExceptionRules(
                listOf(AppTrackerExceptionRule(rule = domain, packageNames = listOf(appPackageName))),
            )
        }
    }

    suspend fun removeRule(
        appPackageName: String,
        domain: String,
    ) = withContext(dispatcherProvider.io()) {
        val rule = blockingDao.getRuleByTrackerDomain(domain)
        rule?.let {
            val updatedRule = it.copy(
                rule = it.rule,
                packageNames = it.packageNames.toMutableSet().apply { remove(appPackageName) }.toList(),
            )
            blockingDao.insertTrackerExceptionRules(listOf(updatedRule))
        }
    }

    suspend fun getAllTrackerRules(): List<AppTrackerExceptionRule> = withContext(dispatcherProvider.io()) {
        return@withContext blockingDao.getTrackerExceptionRules()
    }

    suspend fun deleteAllTrackerRules() = withContext(dispatcherProvider.io()) {
        blockingDao.deleteTrackerExceptionRules()
    }

    suspend fun insertTrackerRules(rules: List<AppTrackerExceptionRule>) = withContext(dispatcherProvider.io()) {
        blockingDao.insertTrackerExceptionRules(rules)
    }
}
