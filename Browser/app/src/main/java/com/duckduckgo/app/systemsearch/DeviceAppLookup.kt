

package com.duckduckgo.app.systemsearch

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.GET_META_DATA
import android.graphics.drawable.Drawable
import androidx.annotation.WorkerThread
import kotlin.text.RegexOption.IGNORE_CASE

data class DeviceApp(
    val shortName: String,
    val packageName: String,
    val launchIntent: Intent,
    private var icon: Drawable? = null,
) {
    fun retrieveIcon(packageManager: PackageManager): Drawable {
        return icon ?: packageManager.getApplicationIcon(packageName).also {
            icon = it
        }
    }
}

interface DeviceAppLookup {
    @WorkerThread
    fun query(query: String): List<DeviceApp>

    @WorkerThread
    fun refreshAppList()
}

class InstalledDeviceAppLookup(private val appListProvider: DeviceAppListProvider) : DeviceAppLookup {

    private var apps: List<DeviceApp>? = null

    @WorkerThread
    override fun query(query: String): List<DeviceApp> {
        if (query.isBlank()) return emptyList()

        if (apps == null) {
            refreshAppList()
        }

        val escapedQuery = Regex.escape(query)
        val wordPrefixMatchingRegex = ".*\\b$escapedQuery.*".toRegex(IGNORE_CASE)
        return apps!!.filter {
            it.shortName.matches(wordPrefixMatchingRegex)
        }.sortedWith(comparator(query))
    }

    private fun comparator(query: String): Comparator<DeviceApp> {
        return compareByDescending<DeviceApp> {
            it.shortName.startsWith(query, ignoreCase = true)
        }.thenBy {
            it.shortName
        }
    }

    @WorkerThread
    override fun refreshAppList() {
        apps = appListProvider.get()
    }
}

interface DeviceAppListProvider {
    @WorkerThread
    fun get(): List<DeviceApp>
}

class InstalledDeviceAppListProvider(private val packageManager: PackageManager) : DeviceAppListProvider {

    @WorkerThread
    override fun get(): List<DeviceApp> {
        val appsInfo = packageManager.getInstalledApplications(GET_META_DATA)

        return appsInfo.map {
            val packageName = it.packageName
            val launchIntent = packageManager.getLaunchIntentForPackage(packageName) ?: return@map null
            val shortName = it.loadLabel(packageManager).toString()
            return@map DeviceApp(shortName, packageName, launchIntent)
        }.filterNotNull()
    }
}
