

package com.duckduckgo.app.browser.pageloadpixel.firstpaint

import com.duckduckgo.app.browser.WebViewPixelName
import com.duckduckgo.app.statistics.api.OfflinePixel
import com.duckduckgo.app.statistics.api.PixelSender
import com.duckduckgo.app.statistics.pixels.Pixel
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import io.reactivex.Completable
import javax.inject.Inject
import timber.log.Timber

@ContributesMultibinding(AppScope::class)
class PagePaintedOfflinePixelSender @Inject constructor(
    private val dao: PagePaintedPixelDao,
    private val pixelSender: PixelSender,
) : OfflinePixel {
    override fun send(): Completable {
        return Completable.defer {
            val pixels = mutableListOf<Completable>()

            val pendingPixels = dao.all()
            pendingPixels.map {
                val params = mapOf(
                    APP_VERSION to it.appVersion,
                    WEBVIEW_VERSION to it.webViewVersion,
                    ELAPSED_TIME_FIRST_PAINT to it.elapsedTimeFirstPaint.toString(),
                )

                val pixel = pixelSender.sendPixel(
                    WebViewPixelName.WEB_PAGE_PAINTED.pixelName,
                    params,
                    mapOf(),
                    Pixel.PixelType.Count,
                ).ignoreElement().doOnComplete {
                    dao.delete(it)
                }
                pixels.add(pixel)
            }
            Timber.v("Sending %d page painted pixels", pixels.size)
            return@defer Completable.mergeDelayError(pixels)
        }
    }

    companion object {
        private const val APP_VERSION = "app_version_when_page_painted"
        private const val WEBVIEW_VERSION = "webview_version"
        private const val ELAPSED_TIME_FIRST_PAINT = "elapsed_time_first_paint"
    }
}
