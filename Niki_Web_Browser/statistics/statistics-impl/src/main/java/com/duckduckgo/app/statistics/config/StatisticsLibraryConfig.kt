

package com.duckduckgo.app.statistics.config

import com.duckduckgo.appbuildconfig.api.AppBuildConfig

/**
 * If you want to configure the statistics library, have your app extend this listener
 * and implement the different methods.
 * The library will check through the application context.
 */
interface StatisticsLibraryConfig {
    fun shouldFirePixelsAsDev(): Boolean
}

class DefaultStatisticsLibraryConfig(private val appBuildConfig: AppBuildConfig) : StatisticsLibraryConfig {
    override fun shouldFirePixelsAsDev(): Boolean {
        return appBuildConfig.isDebug
    }
}
