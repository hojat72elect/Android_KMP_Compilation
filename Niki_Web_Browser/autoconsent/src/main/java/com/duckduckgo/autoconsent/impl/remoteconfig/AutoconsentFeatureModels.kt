package com.duckduckgo.autoconsent.impl.remoteconfig

import com.squareup.moshi.Json

class AutoconsentFeatureModels {

    data class AutoconsentSettings(
        @field:Json(name = "disabledCMPs")
        val disabledCMPs: List<String>,
        @field:Json(name = "compactRuleList")
        val compactRuleList: Any?,
    )
}
