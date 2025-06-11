

package com.duckduckgo.privacy.config.api

/** Public interface for the Privacy Config feature */
interface PrivacyConfig {
    fun privacyConfigData(): PrivacyConfigData?
}

/** Public data class for PrivacyConfig data. */
data class PrivacyConfigData(val version: String, var eTag: String? = null)
