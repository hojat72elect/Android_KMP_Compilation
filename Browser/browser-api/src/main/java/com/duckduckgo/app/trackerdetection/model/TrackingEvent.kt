

package com.duckduckgo.app.trackerdetection.model

data class TrackingEvent(
    val documentUrl: String,
    val trackerUrl: String,
    val categories: List<String>?,
    val entity: Entity?,
    val surrogateId: String?,
    val status: TrackerStatus,
    val type: TrackerType,
)

enum class TrackerType {
    AD,
    OTHER,
}

enum class TrackerStatus {
    BLOCKED,
    USER_ALLOWED,
    AD_ALLOWED,
    SITE_BREAKAGE_ALLOWED,
    SAME_ENTITY_ALLOWED,
    ALLOWED,
}
