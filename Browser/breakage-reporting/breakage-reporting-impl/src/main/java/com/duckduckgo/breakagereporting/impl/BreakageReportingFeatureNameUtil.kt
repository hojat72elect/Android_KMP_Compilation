

package com.duckduckgo.breakagereporting.impl

/**
 * Convenience method to get the [BreakageReporting FeatureName] from its [String] value
 */
fun breakageReportingFeatureValueOf(value: String): BreakageReportingFeatureName? {
    return BreakageReportingFeatureName.values().find { it.value == value }
}
