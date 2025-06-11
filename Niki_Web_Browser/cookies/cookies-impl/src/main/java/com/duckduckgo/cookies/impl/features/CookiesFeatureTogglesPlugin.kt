

package com.duckduckgo.cookies.impl.features

import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.cookies.impl.cookiesFeatureValueOf
import com.duckduckgo.cookies.store.CookiesFeatureToggleRepository
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.FeatureTogglesPlugin
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class CookiesFeatureTogglesPlugin @Inject constructor(
    private val cookiesFeatureToggleRepository: CookiesFeatureToggleRepository,
    private val appBuildConfig: AppBuildConfig,
) : FeatureTogglesPlugin {
    override fun isEnabled(featureName: String, defaultValue: Boolean): Boolean? {
        val cookiesFeature = cookiesFeatureValueOf(featureName) ?: return null
        return cookiesFeatureToggleRepository.get(cookiesFeature, defaultValue) &&
            appBuildConfig.versionCode >= cookiesFeatureToggleRepository.getMinSupportedVersion(cookiesFeature)
    }
}
