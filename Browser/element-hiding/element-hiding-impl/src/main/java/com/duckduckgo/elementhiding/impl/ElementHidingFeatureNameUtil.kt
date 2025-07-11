

package com.duckduckgo.elementhiding.impl

/**
 * Convenience method to get the [ElementHidingFeatureName] from its [String] value
 */
fun elementHidingFeatureValueOf(value: String): ElementHidingFeatureName? {
    return ElementHidingFeatureName.values().find { it.value == value }
}
