

package com.duckduckgo.privacy.config.impl.features.https

import com.duckduckgo.feature.toggles.api.FeatureException

data class HttpsFeature(
    val state: String,
    val minSupportedVersion: Int?,
    val exceptions: List<FeatureException>,
)
