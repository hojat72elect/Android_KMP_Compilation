

package com.duckduckgo.app.anr

import android.util.Base64
import com.duckduckgo.anrs.api.AnrRepository
import com.duckduckgo.app.statistics.api.OfflinePixel
import com.duckduckgo.app.statistics.api.PixelSender
import com.duckduckgo.app.statistics.pixels.Pixel
import com.duckduckgo.app.statistics.pixels.Pixel.PixelType.Count
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import io.reactivex.Completable
import io.reactivex.Completable.complete
import io.reactivex.Completable.defer
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class AnrOfflinePixelSender @Inject constructor(
    private val anrRepository: AnrRepository,
    private val pixelSender: PixelSender,
) : OfflinePixel {
    override fun send(): Completable {
        return defer {
            val anr = anrRepository.peekMostRecentAnr()
            anr?.let {
                val ss = Base64.encodeToString(it.stackTrace.joinToString("\n").toByteArray(), Base64.NO_WRAP or Base64.NO_PADDING or Base64.URL_SAFE)
                return@defer pixelSender.sendPixel(
                    AnrPixelName.ANR_PIXEL.pixelName,
                    mapOf(
                        ANR_STACKTRACE to ss,
                        ANR_WEBVIEW_VERSION to it.webView,
                        ANR_CUSTOM_TAB to it.customTab.toString(),
                    ),
                    mapOf(),
                    Count,
                ).ignoreElement().doOnComplete {
                    anrRepository.removeMostRecentAnr()
                }
            }
            return@defer complete()
        }
    }

    companion object {
        private const val ANR_STACKTRACE = "stackTrace"
        private const val ANR_WEBVIEW_VERSION = "webView"
        private const val ANR_CUSTOM_TAB = "customTab"
    }
}

enum class AnrPixelName(override val pixelName: String) : Pixel.PixelName {
    ANR_PIXEL("m_anr_exception"),
}
