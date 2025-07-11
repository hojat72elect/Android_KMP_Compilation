

package com.duckduckgo.mobile.android.vpn.trackers

import androidx.annotation.WorkerThread
import com.duckduckgo.mobile.android.vpn.dao.VpnAppTrackerBlockingDao
import com.duckduckgo.mobile.android.vpn.dao.VpnAppTrackerSystemAppsOverridesDao
import kotlinx.coroutines.flow.Flow

@WorkerThread
interface AppTrackerRepository {
    fun findTracker(
        hostname: String,
        packageName: String,
    ): AppTrackerType

    fun getAppExclusionList(): List<AppTrackerExcludedPackage>

    fun getAppExclusionListFlow(): Flow<List<AppTrackerExcludedPackage>>

    fun getManualAppExclusionList(): List<AppTrackerManualExcludedApp>

    fun getManualAppExclusionListFlow(): Flow<List<AppTrackerManualExcludedApp>>

    fun getSystemAppOverrideList(): List<AppTrackerSystemAppOverridePackage>

    fun manuallyExcludedApp(packageName: String)

    fun manuallyExcludedApps(packageNames: List<String>)

    fun manuallyEnabledApp(packageName: String)

    fun restoreDefaultProtectedList()
}

class RealAppTrackerRepository(
    private val vpnAppTrackerBlockingDao: VpnAppTrackerBlockingDao,
    private val vpnSystemAppsOverrides: VpnAppTrackerSystemAppsOverridesDao,
) : AppTrackerRepository {

    override fun findTracker(
        hostname: String,
        packageName: String,
    ): AppTrackerType {
        val tracker = vpnAppTrackerBlockingDao.getTrackerBySubdomain(hostname) ?: return AppTrackerType.NotTracker
        val entityName = vpnAppTrackerBlockingDao.getEntityByAppPackageId(packageName)
        if (firstPartyTracker(tracker, entityName)) {
            return AppTrackerType.FirstParty(tracker)
        }

        return AppTrackerType.ThirdParty(tracker)
    }

    private fun firstPartyTracker(
        tracker: AppTracker,
        entityName: AppTrackerPackage?,
    ): Boolean {
        if (entityName == null) return false
        return tracker.owner.name == entityName.entityName
    }

    override fun getAppExclusionList(): List<AppTrackerExcludedPackage> {
        return vpnAppTrackerBlockingDao.getAppExclusionList()
    }

    override fun getAppExclusionListFlow(): Flow<List<AppTrackerExcludedPackage>> {
        return vpnAppTrackerBlockingDao.getAppExclusionListFlow()
    }

    override fun getManualAppExclusionList(): List<AppTrackerManualExcludedApp> {
        return vpnAppTrackerBlockingDao.getManualAppExclusionList()
    }

    override fun getManualAppExclusionListFlow(): Flow<List<AppTrackerManualExcludedApp>> {
        return vpnAppTrackerBlockingDao.getManualAppExclusionListFlow()
    }

    override fun getSystemAppOverrideList(): List<AppTrackerSystemAppOverridePackage> {
        return vpnSystemAppsOverrides.getSystemAppOverrides()
    }

    override fun manuallyExcludedApp(packageName: String) {
        vpnAppTrackerBlockingDao.insertIntoManualAppExclusionList(AppTrackerManualExcludedApp(packageName, false))
    }

    override fun manuallyExcludedApps(packageNames: List<String>) {
        vpnAppTrackerBlockingDao.insertIntoManualAppExclusionList(packageNames.map { AppTrackerManualExcludedApp(it, false) })
    }

    override fun manuallyEnabledApp(packageName: String) {
        vpnAppTrackerBlockingDao.insertIntoManualAppExclusionList(AppTrackerManualExcludedApp(packageName, true))
    }

    override fun restoreDefaultProtectedList() {
        vpnAppTrackerBlockingDao.deleteManualAppExclusionList()
    }
}
