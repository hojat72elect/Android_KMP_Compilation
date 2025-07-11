package com.duckduckgo.contentscopescripts.impl.features.navigatorinterface

/**
 * Convenience method to get the [NavigatorInterfaceFeatureName] from its [String] value
 */
fun navigatorInterfaceFeatureValueOf(value: String): NavigatorInterfaceFeatureName? {
    return NavigatorInterfaceFeatureName.entries.find { it.value == value }
}
