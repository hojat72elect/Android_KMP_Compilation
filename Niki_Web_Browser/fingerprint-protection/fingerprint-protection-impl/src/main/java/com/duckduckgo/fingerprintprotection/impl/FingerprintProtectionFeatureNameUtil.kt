

package com.duckduckgo.fingerprintprotection.impl

import com.duckduckgo.fingerprintprotection.api.FingerprintProtectionFeatureName

/**
 * Convenience method to get the [FingerprintProtectionFeatureName] from its [String] value
 */
fun fingerprintProtectionFeatureValueOf(value: String): FingerprintProtectionFeatureName? {
    return FingerprintProtectionFeatureName.values().find { it.value == value }
}
