

package com.duckduckgo.mobile.android.vpn.logging

import androidx.lifecycle.LifecycleOwner
import com.duckduckgo.app.lifecycle.MainProcessLifecycleObserver
import com.duckduckgo.app.lifecycle.VpnProcessLifecycleObserver
import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.mobile.android.vpn.feature.AppTpLocalFeature
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject
import logcat.AndroidLogcatLogger
import logcat.LogPriority
import logcat.LogcatLogger
import logcat.logcat

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = VpnProcessLifecycleObserver::class,
)
@ContributesMultibinding(
    scope = AppScope::class,
    boundType = MainProcessLifecycleObserver::class,
)
class AndroidLogcatLoggerRegistrar @Inject constructor(
    private val appBuildConfig: AppBuildConfig,
    private val appTpLocalFeature: AppTpLocalFeature,
) : VpnProcessLifecycleObserver, MainProcessLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        onVpnProcessCreated()
    }

    override fun onVpnProcessCreated() {
        if (appBuildConfig.isDebug || appTpLocalFeature.verboseLogging().isEnabled()) {
            LogcatLogger.install(AndroidLogcatLogger(LogPriority.DEBUG))
            logcat { "Registering LogcatLogger" }
        }
    }
}
