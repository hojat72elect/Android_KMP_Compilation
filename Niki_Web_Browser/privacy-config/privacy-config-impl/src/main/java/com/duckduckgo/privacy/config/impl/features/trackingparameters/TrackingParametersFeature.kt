

package com.duckduckgo.privacy.config.impl.features.trackingparameters

import com.duckduckgo.feature.toggles.api.FeatureException

data class TrackingParametersFeature(
    val state: String,
    val minSupportedVersion: Int?,
    val settings: TrackingParametersSettings,
    val exceptions: List<FeatureException>,
)

data class TrackingParametersSettings(
    val parameters: List<String>,
)
