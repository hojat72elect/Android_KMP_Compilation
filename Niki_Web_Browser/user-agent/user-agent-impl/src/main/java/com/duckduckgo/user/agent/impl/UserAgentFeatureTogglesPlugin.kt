

package com.duckduckgo.user.agent.impl

import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.FeatureTogglesPlugin
import com.duckduckgo.user.agent.store.UserAgentFeatureToggleRepository
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class UserAgentFeatureTogglesPlugin @Inject constructor(
    private val userAgentFeatureToggleRepository: UserAgentFeatureToggleRepository,
    private val appBuildConfig: AppBuildConfig,
) : FeatureTogglesPlugin {
    override fun isEnabled(featureName: String, defaultValue: Boolean): Boolean? {
        val userAgentFeature = userAgentFeatureValueOf(featureName) ?: return null
        return userAgentFeatureToggleRepository.get(userAgentFeature, defaultValue) &&
            appBuildConfig.versionCode >= userAgentFeatureToggleRepository.getMinSupportedVersion(userAgentFeature)
    }
}
