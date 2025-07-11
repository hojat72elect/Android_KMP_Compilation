

package com.duckduckgo.privacy.config.impl.plugins

import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.FeatureTogglesPlugin
import com.duckduckgo.privacy.config.impl.features.privacyFeatureValueOf
import com.duckduckgo.privacy.config.store.PrivacyFeatureTogglesRepository
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class PrivacyFeatureTogglesPlugin @Inject constructor(
    private val privacyFeatureTogglesRepository: PrivacyFeatureTogglesRepository,
    private val appBuildConfig: AppBuildConfig,
) : FeatureTogglesPlugin {

    override fun isEnabled(
        featureName: String,
        defaultValue: Boolean,
    ): Boolean? {
        @Suppress("NAME_SHADOWING")
        val privacyFeatureName = privacyFeatureValueOf(featureName) ?: return null
        return privacyFeatureTogglesRepository.get(privacyFeatureName, defaultValue) &&
            appBuildConfig.versionCode >= privacyFeatureTogglesRepository.getMinSupportedVersion(privacyFeatureName)
    }
}
