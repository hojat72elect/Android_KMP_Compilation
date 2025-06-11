

package com.duckduckgo.cookies.impl.features

import com.duckduckgo.feature.toggles.api.FeatureException

data class CookiesFeature(
    val state: String,
    val minSupportedVersion: Int?,
    val exceptions: List<FeatureException>,
    val settings: Settings,
)

data class Settings(
    val firstPartyCookiePolicy: FirstPartyCookiePolicy,
    val thirdPartyCookieNames: List<String>,
)

data class FirstPartyCookiePolicy(
    val threshold: Int,
    val maxAge: Int,
)
