

package com.duckduckgo.privacy.config.impl.features.amplinks

import com.duckduckgo.feature.toggles.api.FeatureException

data class AmpLinksFeature(
    val state: String,
    val minSupportedVersion: Int?,
    val settings: AmpLinkSettings,
    val exceptions: List<FeatureException>,
)

data class AmpLinkSettings(
    val linkFormats: List<String>,
    val keywords: List<String>,
)
