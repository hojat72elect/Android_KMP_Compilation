

package com.duckduckgo.user.agent.impl

import com.duckduckgo.feature.toggles.api.FeatureException

data class UserAgentFeature(
    val state: String,
    val minSupportedVersion: Int?,
    val exceptions: List<FeatureException>,
)
