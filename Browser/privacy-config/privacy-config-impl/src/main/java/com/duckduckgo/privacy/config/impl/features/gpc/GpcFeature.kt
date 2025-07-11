

package com.duckduckgo.privacy.config.impl.features.gpc

import com.duckduckgo.privacy.config.store.GpcExceptionEntity

data class GpcFeature(
    val state: String,
    val minSupportedVersion: Int?,
    val exceptions: List<GpcExceptionEntity>,
    val settings: GpcSettings,
)

data class GpcSettings(val gpcHeaderEnabledSites: List<String>)
