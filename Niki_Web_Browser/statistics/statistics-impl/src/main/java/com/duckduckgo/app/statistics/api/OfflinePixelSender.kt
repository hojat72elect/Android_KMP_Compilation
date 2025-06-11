

package com.duckduckgo.app.statistics.api

import com.duckduckgo.di.DaggerSet
import io.reactivex.Completable
import io.reactivex.Completable.*

/**
 * Most pixels are "send and forget" however we sometimes need to guarantee that a pixel will be
 * sent. In those cases we schedule them to happen as part of our app data sync.
 */
class OfflinePixelSender constructor(
    private val offlinePixels: DaggerSet<OfflinePixel>,
) {

    fun sendOfflinePixels(): Completable {
        return mergeDelayError(
            listOf(*offlinePixels.map { it.send() }.toTypedArray()),
        )
    }
}
