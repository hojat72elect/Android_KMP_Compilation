

package com.duckduckgo.app.global.model

import com.duckduckgo.app.trackerdetection.model.Entity

data class SitePrivacyData(
    val url: String,
    val entity: Entity?,
    val prevalence: Double?,
)
