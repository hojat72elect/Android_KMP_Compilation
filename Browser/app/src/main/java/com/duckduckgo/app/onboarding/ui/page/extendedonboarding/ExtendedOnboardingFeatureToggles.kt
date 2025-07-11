

package com.duckduckgo.app.onboarding.ui.page.extendedonboarding

import com.duckduckgo.anvil.annotations.ContributesRemoteFeature
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.Toggle
import com.duckduckgo.feature.toggles.api.Toggle.DefaultFeatureValue
import com.duckduckgo.feature.toggles.api.Toggle.Experiment

@ContributesRemoteFeature(
    scope = AppScope::class,
    featureName = "extendedOnboarding",
)
interface ExtendedOnboardingFeatureToggles {

    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun self(): Toggle

    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun noBrowserCtas(): Toggle

    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun privacyProCta(): Toggle

    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    @Experiment
    fun highlights(): Toggle
}
