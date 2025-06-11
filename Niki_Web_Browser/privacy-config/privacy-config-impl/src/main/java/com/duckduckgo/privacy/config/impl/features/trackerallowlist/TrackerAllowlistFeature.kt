

package com.duckduckgo.privacy.config.impl.features.trackerallowlist

import com.duckduckgo.privacy.config.store.AllowlistRuleEntity

data class TrackerAllowlistFeature(
    val state: String,
    val minSupportedVersion: Int?,
    val settings: TrackerAllowSettings,
)

data class TrackerAllowSettings(val allowlistedTrackers: Map<String, AllowListedTrackers>)

data class AllowListedTrackers(val rules: List<AllowlistRuleEntity>)
