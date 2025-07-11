

package com.duckduckgo.privacy.config.impl.features.contentblocking

import com.duckduckgo.feature.toggles.api.FeatureException

data class ContentBlockingFeature(
    val state: String,
    val minSupportedVersion: Int?,
    val exceptions: List<FeatureException>,
)
