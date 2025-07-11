

package com.duckduckgo.mobile.android.vpn.trackers

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

class AppTrackerJsonParser {

    companion object {

        fun parseAppTrackerJson(moshi: Moshi, json: String): AppTrackerBlocklist {
            val adapter: JsonAdapter<JsonAppBlockingList> =
                moshi.adapter(JsonAppBlockingList::class.java)
            val parsed = adapter.fromJson(json)
            val version = parseBlocklistVersion(parsed)
            val appTrackers = parseAppTrackers(parsed)
            val appPackages = parseAppPackages(parsed)
            val entities = parseTrackerEntities(parsed)
            return AppTrackerBlocklist(version, appTrackers, appPackages, entities)
        }

        private fun parseBlocklistVersion(parsed: JsonAppBlockingList?) = parsed?.version.orEmpty()

        fun parseAppTrackers(parsed: JsonAppBlockingList?) =
            parsed
                ?.trackers
                .orEmpty()
                .filter { it.value.defaultAction == "block" }
                .mapValues {
                    AppTracker(
                        hostname = it.key,
                        trackerCompanyId = it.value.owner.name.hashCode(),
                        owner = it.value.owner,
                        app = TrackerApp(1, 1.0),
                    )
                }
                .map { it.value }

        fun parseAppPackages(response: JsonAppBlockingList?): List<AppTrackerPackage> {
            return response
                ?.packageNames
                .orEmpty()
                .mapValues { AppTrackerPackage(packageName = it.key, entityName = it.value) }
                .map { it.value }
        }

        fun parseTrackerEntities(response: JsonAppBlockingList?): List<AppTrackerEntity> {
            return response
                ?.entities
                .orEmpty()
                .mapValues {
                    AppTrackerEntity(
                        trackerCompanyId = it.key.hashCode(),
                        entityName = it.key,
                        score = it.value.score,
                        signals = it.value.signals,
                    )
                }
                .map { it.value }
                .sortedBy { it.score }
        }
    }
}
