

package com.duckduckgo.mobile.android.vpn.blocklist

import androidx.annotation.WorkerThread
import com.duckduckgo.common.utils.extensions.extractETag
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.mobile.android.vpn.trackers.*
import com.duckduckgo.mobile.android.vpn.trackers.AppTracker
import com.duckduckgo.mobile.android.vpn.trackers.AppTrackerJsonParser
import com.duckduckgo.mobile.android.vpn.trackers.AppTrackerPackage
import com.duckduckgo.mobile.android.vpn.trackers.JsonAppBlockingList
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import logcat.LogPriority
import logcat.logcat
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

data class AppTrackerBlocklist(
    val etag: ETag = ETag.InvalidETag,
    val blocklist: List<AppTracker> = listOf(),
    val appPackages: List<AppTrackerPackage> = listOf(),
    val entities: List<AppTrackerEntity> = listOf(),
)

sealed class ETag {
    object InvalidETag : ETag()
    data class ValidETag(val value: String) : ETag()
}

interface AppTrackerListDownloader {
    @WorkerThread
    fun downloadAppTrackerBlocklist(): AppTrackerBlocklist
}

@ContributesBinding(AppScope::class)
class RealAppTrackerListDownloader @Inject constructor(
    private val appTrackerListService: AppTrackerListService,
) : AppTrackerListDownloader {
    override fun downloadAppTrackerBlocklist(): AppTrackerBlocklist {
        logcat { "Downloading the app tracker blocklist..." }
        val response = runCatching {
            appTrackerListService.appTrackerBlocklist().execute()
        }.getOrElse {
            logcat(LogPriority.WARN) { "Error downloading tracker rules list: $it" }
            Response.error(400, "".toResponseBody(null))
        }

        if (!response.isSuccessful) {
            logcat(LogPriority.WARN) { "Fail to download the app tracker blocklist, error code: ${response.code()}" }
            return AppTrackerBlocklist()
        }

        val eTag = response.headers().extractETag()
        val responseBody = response.body()

        val blocklist = extractBlocklist(responseBody)
        val packages = extractAppPackages(responseBody)
        val trackerEntities = extractTrackerEntities(responseBody)

        logcat {
            "Received the app tracker remote lists. blocklist size: ${blocklist.size}, " +
                "app-packages size: ${packages.size}, entities size: ${trackerEntities.size}"
        }

        return AppTrackerBlocklist(etag = ETag.ValidETag(eTag), blocklist = blocklist, appPackages = packages, entities = trackerEntities)
    }

    private fun extractBlocklist(response: JsonAppBlockingList?): List<AppTracker> {
        return AppTrackerJsonParser.parseAppTrackers(response)
    }

    private fun extractAppPackages(response: JsonAppBlockingList?): List<AppTrackerPackage> {
        return AppTrackerJsonParser.parseAppPackages(response)
    }

    private fun extractTrackerEntities(response: JsonAppBlockingList?): List<AppTrackerEntity> {
        return AppTrackerJsonParser.parseTrackerEntities(response)
    }
}
