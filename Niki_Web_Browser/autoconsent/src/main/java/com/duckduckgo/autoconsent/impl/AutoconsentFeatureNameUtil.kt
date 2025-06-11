package com.duckduckgo.autoconsent.impl

import com.duckduckgo.autoconsent.api.AutoconsentFeatureName

/**
 * Convenience method to get the [AutoconsentFeatureName] from its [String] value
 */
fun autoconsentFeatureValueOf(value: String): AutoconsentFeatureName? {
    return AutoconsentFeatureName.values().find { it.value == value }
}
