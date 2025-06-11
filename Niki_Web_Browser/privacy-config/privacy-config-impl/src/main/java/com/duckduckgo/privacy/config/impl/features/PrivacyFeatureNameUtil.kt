

package com.duckduckgo.privacy.config.impl.features

import com.duckduckgo.privacy.config.api.PrivacyFeatureName

/**
 * Convenience method to get the [PrivacyFeatureName] from its [String] value
 */
fun privacyFeatureValueOf(value: String): PrivacyFeatureName? {
    return PrivacyFeatureName.values().find { it.value == value }
}
