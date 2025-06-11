

package com.duckduckgo.app.browser.trafficquality

import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface AppVersionHeaderProvider {
    fun provide(isStub: Boolean): String
}

@ContributesBinding(AppScope::class)
class RealAppVersionHeaderProvider @Inject constructor(
    private val appBuildConfig: AppBuildConfig,

) : AppVersionHeaderProvider {
    override fun provide(isStub: Boolean): String {
        return if (isStub) {
            APP_VERSION_QUALITY_DEFAULT_VALUE
        } else {
            appBuildConfig.versionName
        }
    }

    companion object {
        const val APP_VERSION_QUALITY_DEFAULT_VALUE = "other_versions"
    }
}
