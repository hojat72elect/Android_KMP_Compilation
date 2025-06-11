

package com.duckduckgo.downloads.impl

import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import logcat.logcat
import okhttp3.ResponseBody
import retrofit2.Call

interface UrlFileDownloadCallManager {
    fun add(downloadId: Long, call: Call<ResponseBody>)

    fun remove(downloadId: Long)
}

@ContributesBinding(
    scope = AppScope::class,
    boundType = UrlFileDownloadCallManager::class,
)
@SingleInstanceIn(AppScope::class)
class RealUrlFileDownloadCallManager @Inject constructor() : UrlFileDownloadCallManager {
    private val callsMap = ConcurrentHashMap<Long, Call<ResponseBody>>()

    /**
     * Call this method to cancel and remove an ongoing download, or to clean up the [downloadId] from the list of downloads.
     * It is safe to call this method multiple times with the same [downloadId].
     */
    override fun remove(downloadId: Long) {
        logcat { "Removing download $downloadId" }
        callsMap[downloadId]?.cancel()
        callsMap.remove(downloadId)
    }

    /**
     * Call this method to add the ongoing download to the list of downloads.
     * The ongoing download is identified by its [downloadId] and the network [call].
     */
    override fun add(downloadId: Long, call: Call<ResponseBody>) {
        logcat { "Adding download $downloadId" }
        callsMap[downloadId] = call
    }
}
