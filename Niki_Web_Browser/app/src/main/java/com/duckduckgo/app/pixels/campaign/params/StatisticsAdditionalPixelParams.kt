

package com.duckduckgo.app.pixels.campaign.params

import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class ReinstallAdditionalPixelParamPlugin @Inject constructor(
    private val appBuildConfig: AppBuildConfig,
) : AdditionalPixelParamPlugin {
    override suspend fun params(): Pair<String, String> = Pair(
        "isReinstall",
        "${appBuildConfig.isAppReinstall()}",
    )
}
