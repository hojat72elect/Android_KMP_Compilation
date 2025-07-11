

package com.duckduckgo.mobile.android.vpn.ui.tracker_activity.model

import com.duckduckgo.mobile.android.vpn.apps.ui.TrackingProtectionExclusionListActivity
import com.duckduckgo.mobile.android.vpn.model.TrackingApp

sealed class TrackerFeedItem(open val id: Int) {
    data class TrackerFeedData(
        override val id: Int,
        val bucket: String,
        val trackingApp: TrackingApp,
        val trackingCompanyBadges: List<TrackerCompanyBadge>,
        val trackersTotalCount: Int,
        val timestamp: String,
        val displayTimestamp: String,
    ) : TrackerFeedItem(id)

    object TrackerLoadingSkeleton : TrackerFeedItem(0)

    object TrackerDescriptionFeed : TrackerFeedItem(0)

    data class TrackerTrackerAppsProtection(
        val appsData: AppsProtectionData,
        val selectedFilter: TrackingProtectionExclusionListActivity.Companion.AppsFilter? = null,
    ) : TrackerFeedItem(0)

    data class TrackerFeedItemHeader(val timestamp: String) : TrackerFeedItem(timestamp.hashCode())
}

sealed class TrackerCompanyBadge {
    data class Company(
        val companyName: String,
        val companyDisplayName: String,
    ) : TrackerCompanyBadge()

    data class Extra(
        val amount: Int,
    ) : TrackerCompanyBadge()
}

data class AppsData(
    val appsCount: Int,
    val isProtected: Boolean,
    val packageNames: List<String>,
)

data class AppsProtectionData(
    val protectedAppsData: AppsData,
    val unprotectedAppsData: AppsData,
)
