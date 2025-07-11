

package com.duckduckgo.feature.toggles.api

/**
 * Features that can be enabled/disabled should implement this plugin. The associated plugin point
 * will call the plugins when the [FeatureToggle] API is used
 */
@Deprecated(
    message = "Use the new feature flag framework, https://app.asana.com/0/1202552961248957/1203898052213029/f",
    level = DeprecationLevel.WARNING,
)
interface FeatureTogglesPlugin {
    /**
     * This method will return whether the plugin knows about the [featureName] in which case will
     * return whether it is enabled or disabled
     * @return `true` if the feature is enable. `false` when disabled. `null` if the plugin does not
     * know the featureName. [defaultValue] if the plugin knows featureName but is not set
     */
    fun isEnabled(
        featureName: String,
        defaultValue: Boolean,
    ): Boolean?
}
