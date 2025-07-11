

package com.duckduckgo.app.browser.pageloadpixel

import com.duckduckgo.app.browser.WebViewPixelName
import com.duckduckgo.app.statistics.api.OfflinePixel
import com.duckduckgo.app.statistics.api.PixelSender
import com.duckduckgo.app.statistics.pixels.Pixel.PixelType.Count
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import io.reactivex.Completable
import javax.inject.Inject
import timber.log.Timber

private const val ELAPSED_TIME = "elapsed_time"
private const val WEBVIEW_VERSION = "webview_version"
private const val CPM_ENABLED = "cpm_enabled"
private const val TRACKER_OPTIMIZATION_ENABLED_V2 = "tracker_optimization_enabled_v2"

// This is used to ensure the app version we send is the one from the moment the page was loaded, and not then the pixel is fired later on
private const val APP_VERSION = "app_version_when_page_loaded"

@ContributesMultibinding(AppScope::class)
class PageLoadedOfflinePixelSender @Inject constructor(
    private val pageLoadedPixelDao: PageLoadedPixelDao,
    private val pixelSender: PixelSender,
) : OfflinePixel {
    override fun send(): Completable {
        return Completable.defer {
            val pixels = mutableListOf<Completable>()

            val pendingPixels = pageLoadedPixelDao.all()
            pendingPixels.map {
                val params = mapOf(
                    APP_VERSION to it.appVersion,
                    ELAPSED_TIME to it.elapsedTime.toString(),
                    WEBVIEW_VERSION to it.webviewVersion,
                    CPM_ENABLED to it.cpmEnabled.toString(),
                    TRACKER_OPTIMIZATION_ENABLED_V2 to it.trackerOptimizationEnabled.toString(),
                )

                val pixel = pixelSender.sendPixel(
                    WebViewPixelName.WEB_PAGE_LOADED.pixelName,
                    params,
                    mapOf(),
                    Count,
                ).ignoreElement().doOnComplete {
                    Timber.d("Sent page loaded pixel with params: $params")
                    pageLoadedPixelDao.delete(it)
                }
                pixels.add(pixel)
            }
            return@defer Completable.mergeDelayError(pixels)
        }
    }
}
