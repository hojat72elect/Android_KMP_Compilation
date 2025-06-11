package com.duckduckgo.adclick.impl.pixels

import com.duckduckgo.app.statistics.pixels.Pixel

enum class AdClickPixelName(override val pixelName: String) : Pixel.PixelName {
    AD_CLICK_DETECTED("m_ad_click_detected"),
    AD_CLICK_ACTIVE("m_ad_click_active"),
    AD_CLICK_PAGELOADS_WITH_AD_ATTRIBUTION("m_pageloads_with_ad_attribution"),
}

object AdClickPixelValues {
    const val AD_CLICK_DETECTED_MATCHED = "matched"
    const val AD_CLICK_DETECTED_MISMATCH = "mismatch"
    const val AD_CLICK_DETECTED_SERP_ONLY = "serp_only"
    const val AD_CLICK_DETECTED_HEURISTIC_ONLY = "heuristic_only"
    const val AD_CLICK_DETECTED_NONE = "none"
}

object AdClickPixelParameters {
    const val AD_CLICK_DOMAIN_DETECTION = "domainDetection"
    const val AD_CLICK_HEURISTIC_DETECTION = "heuristicDetection"
    const val AD_CLICK_DOMAIN_DETECTION_ENABLED = "domainDetectionEnabled"
    const val AD_CLICK_PAGELOADS_WITH_AD_ATTRIBUTION_COUNT = "count"
}
