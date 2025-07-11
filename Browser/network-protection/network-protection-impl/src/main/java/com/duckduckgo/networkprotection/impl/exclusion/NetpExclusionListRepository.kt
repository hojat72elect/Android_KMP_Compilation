

package com.duckduckgo.networkprotection.impl.exclusion

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.networkprotection.impl.autoexclude.AutoExcludeAppsRepository
import com.duckduckgo.networkprotection.impl.settings.NetPSettingsLocalConfig
import com.duckduckgo.networkprotection.store.NetPManualExclusionListRepository
import com.duckduckgo.networkprotection.store.db.NetPManuallyExcludedApp
import com.duckduckgo.networkprotection.store.db.VpnIncompatibleApp
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject
import kotlinx.coroutines.withContext

interface NetPExclusionListRepository {
    suspend fun getExcludedAppPackages(): List<String>
}

@ContributesBinding(AppScope::class)
@SingleInstanceIn(AppScope::class)
class RealNetPExclusionListRepository @Inject constructor(
    private val manualExclusionListRepository: NetPManualExclusionListRepository,
    private val autoExcludeAppsRepository: AutoExcludeAppsRepository,
    private val netPSettingsLocalConfig: NetPSettingsLocalConfig,
    private val dispatcherProvider: DispatcherProvider,
    private val packageManager: PackageManager,
) : NetPExclusionListRepository {
    override suspend fun getExcludedAppPackages(): List<String> {
        return withContext(dispatcherProvider.io()) {
            val manuallyExcludedApps = manualExclusionListRepository.getManualAppExclusionList()
            val autoExcludeApps = if (isAutoExcludeEnabled()) {
                autoExcludeAppsRepository.getAllIncompatibleApps()
            } else {
                emptyList()
            }

            packageManager.getInstalledApplications(PackageManager.GET_META_DATA).asSequence()
                .filter { isExcludedFromVpn(it, manuallyExcludedApps, autoExcludeApps) }
                .sortedBy { it.name }
                .map { it.packageName }
                .toList()
        }
    }

    private fun isAutoExcludeEnabled(): Boolean {
        return netPSettingsLocalConfig.autoExcludeBrokenApps().isEnabled()
    }

    private fun isExcludedFromVpn(
        appInfo: ApplicationInfo,
        manualExclusionList: List<NetPManuallyExcludedApp>,
        autoExcludeList: List<VpnIncompatibleApp>,
    ): Boolean {
        val userExcludedApp = manualExclusionList.find { it.packageId == appInfo.packageName }
        if (userExcludedApp != null) {
            return !userExcludedApp.isProtected
        }

        return autoExcludeList.isNotEmpty() && autoExcludeList.any { it.packageName == appInfo.packageName }
    }
}
