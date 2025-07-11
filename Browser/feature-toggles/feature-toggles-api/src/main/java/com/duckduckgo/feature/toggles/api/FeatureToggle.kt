

package com.duckduckgo.feature.toggles.api

/** Any feature toggles implemented in any module should implement [FeatureToggle] */
@Deprecated(
    message = "Use the new feature flag framework, https://app.asana.com/0/1202552961248957/1203898052213029/f",
    level = DeprecationLevel.WARNING,
)
interface FeatureToggle {
    /**
     * This method takes a [featureName] and optionally a default value.
     * @return `true` if the feature is enabled, `false` if is not
     * @throws [IllegalArgumentException] if the feature is not implemented
     */
    fun isFeatureEnabled(
        featureName: String,
        defaultValue: Boolean = true,
    ): Boolean
}
