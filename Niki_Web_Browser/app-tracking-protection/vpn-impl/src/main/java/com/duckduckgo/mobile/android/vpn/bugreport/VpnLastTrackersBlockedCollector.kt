

package com.duckduckgo.mobile.android.vpn.bugreport

import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.common.utils.formatters.time.DatabaseDateFormatter
import com.duckduckgo.common.utils.formatters.time.model.dateOfLastDay
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.mobile.android.vpn.state.VpnStateCollectorPlugin
import com.duckduckgo.mobile.android.vpn.stats.AppTrackerBlockingStatsRepository
import com.squareup.anvil.annotations.ContributesMultibinding
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.time.LocalDateTime
import javax.inject.Inject
import kotlinx.coroutines.withContext
import org.json.JSONObject

@ContributesMultibinding(ActivityScope::class)
class VpnLastTrackersBlockedCollector @Inject constructor(
    private val appTrackerBlockingRepository: AppTrackerBlockingStatsRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val moshi: Moshi,
) : VpnStateCollectorPlugin {

    override val collectorName: String
        get() = "trackersBlockedLastDay"

    override suspend fun collectVpnRelatedState(appPackageId: String?): JSONObject {
        return withContext(dispatcherProvider.io()) {
            val result = mutableMapOf<String, List<String>>()
            appTrackerBlockingRepository.getVpnTrackersSync({ dateOfLastDay() }, noEndDate())
                .filter { tracker -> appPackageId?.let { tracker.trackingApp.packageId == it } ?: true }
                .groupBy { it.trackingApp.packageId }
                .mapValues { entry -> entry.value.map { it.domain } }
                .onEach {
                    result[it.key] = it.value.toSet().toList()
                }

            val adapter = moshi.adapter<Map<String, List<String>>>(
                Types.newParameterizedType(
                    Map::class.java,
                    String::class.java,
                    List::class.java,
                    String::class.java,
                ),
            )
            return@withContext JSONObject(adapter.toJson(result))
        }
    }

    private fun noEndDate(): String {
        return DatabaseDateFormatter.timestamp(LocalDateTime.of(9999, 1, 1, 0, 0))
    }
}
