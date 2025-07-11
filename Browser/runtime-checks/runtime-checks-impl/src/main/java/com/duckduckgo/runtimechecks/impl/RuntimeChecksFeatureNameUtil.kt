

package com.duckduckgo.runtimechecks.impl

/**
 * Convenience method to get the [RuntimeChecksFeatureName] from its [String] value
 */
fun runtimeChecksFeatureValueOf(value: String): RuntimeChecksFeatureName? {
    return RuntimeChecksFeatureName.values().find { it.value == value }
}
