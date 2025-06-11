

package com.duckduckgo.networkprotection.internal.network

import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.core.content.edit
import com.duckduckgo.data.store.api.SharedPreferencesProvider
import com.duckduckgo.networkprotection.internal.feature.NetPInternalFeatureToggles
import javax.inject.Inject

class NetPInternalExclusionListProvider @Inject constructor(
    private val packageManager: PackageManager,
    private val netPInternalFeatureToggles: NetPInternalFeatureToggles,
    private val sharedPreferencesProvider: SharedPreferencesProvider,
) {
    private val preferences: SharedPreferences by lazy {
        sharedPreferencesProvider.getSharedPreferences(
            FILENAME,
            multiprocess = true,
            migrate = false,
        )
    }

    internal fun getExclusionList(): Set<String> {
        if (!netPInternalFeatureToggles.excludeSystemApps().isEnabled()) return excludeManuallySelectedApps()

        // returns the list of system apps for now
        return packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            .asSequence()
            .filter { (it.flags and ApplicationInfo.FLAG_SYSTEM) != 0 }
            .map { it.packageName }
            .toSet()
    }

    internal fun excludeSystemApp(packageName: String) {
        preferences.edit { putBoolean(packageName, true) }
    }

    internal fun includeSystemApp(packageName: String) {
        preferences.edit { remove(packageName) }
    }

    private fun excludeManuallySelectedApps(): Set<String> {
        return preferences.all.keys
    }
}

private const val FILENAME = "com.duckduckgo.netp.internal.excluded_system_apps.v1"
