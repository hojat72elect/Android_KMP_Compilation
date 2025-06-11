

package com.duckduckgo.request.filterer.impl

import com.duckduckgo.request.filterer.api.RequestFiltererFeatureName

/**
 * Convenience method to get the [RequestFiltererFeatureName] from its [String] value
 */
fun requestFiltererFeatureValueOf(value: String): RequestFiltererFeatureName? {
    return RequestFiltererFeatureName.values().find { it.value == value }
}
