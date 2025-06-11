

package com.duckduckgo.privacy.config.impl.models

import com.duckduckgo.feature.toggles.api.FeatureException
import org.json.JSONObject

data class JsonPrivacyConfig(
    val version: Long,
    val readme: String,
    val features: Map<String, JSONObject?>,
    val unprotectedTemporary: List<FeatureException>,
    val experimentalVariants: JSONObject?,
)
