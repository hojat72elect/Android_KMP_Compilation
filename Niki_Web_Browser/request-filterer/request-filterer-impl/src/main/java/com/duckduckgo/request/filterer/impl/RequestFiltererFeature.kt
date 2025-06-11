

package com.duckduckgo.request.filterer.impl

import com.duckduckgo.feature.toggles.api.FeatureException

data class RequestFiltererFeature(
    val state: String,
    val minSupportedVersion: Int?,
    val exceptions: List<FeatureException>,
    val settings: Settings,
)

data class Settings(
    val windowInMs: Int,
)
