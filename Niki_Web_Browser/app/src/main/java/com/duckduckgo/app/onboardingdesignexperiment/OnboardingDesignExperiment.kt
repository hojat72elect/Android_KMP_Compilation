

package com.duckduckgo.app.onboardingdesignexperiment

import com.duckduckgo.anvil.annotations.ContributesRemoteFeature
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.Toggle
import com.duckduckgo.feature.toggles.api.Toggle.DefaultFeatureValue

@ContributesRemoteFeature(
    scope = AppScope::class,
    featureName = "onboardingDesignExperiment",
)
/**
 * Interface defining feature toggles for the onboarding design experiment.
 * These toggles control specific features related to the onboarding process.
 */
interface OnboardingDesignExperimentToggles {

    /**
     * Toggle for enabling or disabling the "self" onboarding design experiment.
     * Default value: false (disabled).
     */
    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun self(): Toggle

    /**
     * Toggle for enabling or disabling the "buckOnboarding" design experiment.
     * Default value: false (disabled).
     */
    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun buckOnboarding(): Toggle
}
