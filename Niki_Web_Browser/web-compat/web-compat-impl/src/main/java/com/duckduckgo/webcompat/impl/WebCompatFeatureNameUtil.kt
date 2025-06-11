

package com.duckduckgo.webcompat.impl

/**
 * Convenience method to get the [WebCompatFeatureName] from its [String] value
 */
fun webCompatFeatureValueOf(value: String): WebCompatFeatureName? {
    return WebCompatFeatureName.values().find { it.value == value }
}
