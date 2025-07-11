package com.duckduckgo.contentscopescripts.impl.features.messagebridge

/**
 * Convenience method to get the [MessageBridgeFeatureName] from its [String] value
 */
fun messageBridgeFeatureValueOf(value: String): MessageBridgeFeatureName? {
    return MessageBridgeFeatureName.entries.find { it.value == value }
}
