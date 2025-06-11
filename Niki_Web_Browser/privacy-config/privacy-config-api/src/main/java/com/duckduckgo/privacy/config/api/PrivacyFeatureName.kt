

package com.duckduckgo.privacy.config.api

/** List of [PrivacyFeatureName] that belong to the Privacy Configuration */
enum class PrivacyFeatureName(val value: String) {
    ContentBlockingFeatureName("contentBlocking"),
    GpcFeatureName("gpc"),
    HttpsFeatureName("https"),
    TrackerAllowlistFeatureName("trackerAllowlist"),
    DrmFeatureName("eme"),
    AmpLinksFeatureName("ampLinks"),
    TrackingParametersFeatureName("trackingParameters"),
}

const val PRIVACY_REMOTE_CONFIG_URL = "https://staticcdn.duckduckgo.com/trackerblocking/config/v4/android-config.json"
