

package com.duckduckgo.app.anr

import com.duckduckgo.anrs.api.CrashLogger
import com.duckduckgo.app.anrs.store.UncaughtExceptionDao
import com.duckduckgo.app.di.ProcessName
import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.browser.api.WebViewVersionProvider
import com.duckduckgo.common.utils.checkMainThread
import com.duckduckgo.customtabs.api.CustomTabDetector
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class RealCrashLogger @Inject constructor(
    private val appBuildConfig: AppBuildConfig,
    private val uncaughtExceptionDao: UncaughtExceptionDao,
    private val webViewVersionProvider: WebViewVersionProvider,
    private val customTabDetector: CustomTabDetector,
    @ProcessName private val processName: String,
) : CrashLogger {
    override fun logCrash(crash: CrashLogger.Crash) {
        checkMainThread()

        uncaughtExceptionDao.add(
            crash.asCrashEntity(
                appBuildConfig.versionName,
                processName,
                webViewVersionProvider.getFullVersion(),
                customTabDetector.isCustomTab(),
            ),
        )
    }
}
