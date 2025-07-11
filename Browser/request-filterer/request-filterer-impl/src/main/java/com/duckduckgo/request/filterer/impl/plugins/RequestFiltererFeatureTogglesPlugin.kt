

package com.duckduckgo.request.filterer.impl.plugins

import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.FeatureTogglesPlugin
import com.duckduckgo.request.filterer.impl.requestFiltererFeatureValueOf
import com.duckduckgo.request.filterer.store.RequestFiltererFeatureToggleRepository
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class RequestFiltererFeatureTogglesPlugin @Inject constructor(
    private val requestFiltererFeatureToggleRepository: RequestFiltererFeatureToggleRepository,
    private val appBuildConfig: AppBuildConfig,
) : FeatureTogglesPlugin {
    override fun isEnabled(featureName: String, defaultValue: Boolean): Boolean? {
        val cookiesFeature = requestFiltererFeatureValueOf(featureName) ?: return null
        return requestFiltererFeatureToggleRepository.get(cookiesFeature, defaultValue) &&
            appBuildConfig.versionCode >= requestFiltererFeatureToggleRepository.getMinSupportedVersion(cookiesFeature)
    }
}
