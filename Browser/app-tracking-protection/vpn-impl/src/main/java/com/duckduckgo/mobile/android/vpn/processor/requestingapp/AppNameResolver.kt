

package com.duckduckgo.mobile.android.vpn.processor.requestingapp

import android.content.pm.PackageManager
import logcat.LogPriority
import logcat.asLog
import logcat.logcat

private const val UNKNOWN = "unknown"

interface AppNameResolver {
    /**
     * @return the [OriginatingApp] for a given packageID or [OriginatingApp.unknown] when packageID is not known
     */
    fun getAppNameForPackageId(packageId: String): OriginatingApp

    /**
     * @return returns the package name for a given UID or `null` if it is unknown
     */
    fun getPackageIdForUid(uid: Int): String?

    data class OriginatingApp(
        val packageId: String,
        val appName: String,
    ) {
        override fun toString(): String = "package=$packageId ($appName)"

        fun isUnknown(): Boolean {
            return UNKNOWN.equals(appName, ignoreCase = true)
        }

        companion object {
            fun unknown(): OriginatingApp {
                return OriginatingApp(UNKNOWN, UNKNOWN)
            }
        }
    }
}

internal class RealAppNameResolver(private val packageManager: PackageManager) : AppNameResolver {

    override fun getAppNameForPackageId(packageId: String): AppNameResolver.OriginatingApp {
        val stripped = packageId.substringBefore(":")
        return try {
            val appName = packageManager.getApplicationLabel(packageManager.getApplicationInfo(stripped, PackageManager.GET_META_DATA)) as String
            AppNameResolver.OriginatingApp(packageId, appName)
        } catch (e: PackageManager.NameNotFoundException) {
            logcat(LogPriority.ERROR) { "Failed to find app name for: $stripped. ${e.message}" }
            AppNameResolver.OriginatingApp(packageId, UNKNOWN)
        }
    }

    override fun getPackageIdForUid(uid: Int): String? {
        val packages: Array<String>?

        try {
            packages = packageManager.getPackagesForUid(uid)
        } catch (e: SecurityException) {
            logcat(LogPriority.ERROR) { e.asLog() }
            return null
        }

        if (packages.isNullOrEmpty()) {
            logcat(LogPriority.WARN) { "Failed to get package ID for UID: $uid" }
            return null
        }

        if (packages.size > 1) {
            val sb = StringBuilder(String.format("Found %d packages for uid:%d", packages.size, uid))
            packages.forEach {
                sb.append(String.format("\npackage: %s", it))
            }
            logcat { sb.toString() }
        }

        return packages.firstOrNull()
    }
}
