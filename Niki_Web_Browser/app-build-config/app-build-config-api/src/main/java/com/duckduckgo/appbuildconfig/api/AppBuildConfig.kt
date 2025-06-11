

package com.duckduckgo.appbuildconfig.api

import java.util.*

interface AppBuildConfig {
    val isTest: Boolean
    val isPerformanceTest: Boolean
    val isDebug: Boolean
    val applicationId: String
    val buildType: String
    val versionCode: Int
    val versionName: String
    val flavor: BuildFlavor
    val sdkInt: Int
    val manufacturer: String
    val model: String
    val deviceLocale: Locale
    val isDefaultVariantForced: Boolean
    val buildDateTimeMillis: Long
    val canSkipOnboarding: Boolean

    /**
     * You should call [variantName] in a background thread
     */
    val variantName: String?

    /**
     * @return `true` if the user re-installed the app, `false` otherwise
     */
    suspend fun isAppReinstall(): Boolean
}

enum class BuildFlavor {
    INTERNAL,
    PLAY,
}

/**
 * Convenience extension function
 * @return `true` when the current build flavor is INTERNAL, `false` otherwise
 */
fun AppBuildConfig.isInternalBuild(): Boolean = flavor == BuildFlavor.INTERNAL
