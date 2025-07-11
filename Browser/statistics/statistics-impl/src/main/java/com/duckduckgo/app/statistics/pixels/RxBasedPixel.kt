

package com.duckduckgo.app.statistics.pixels

import android.annotation.SuppressLint
import com.duckduckgo.app.statistics.api.PixelSender
import com.duckduckgo.app.statistics.api.PixelSender.SendPixelResult.PIXEL_IGNORED
import com.duckduckgo.app.statistics.api.PixelSender.SendPixelResult.PIXEL_SENT
import com.duckduckgo.app.statistics.pixels.Pixel.PixelType
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import timber.log.Timber

@ContributesBinding(AppScope::class)
class RxBasedPixel @Inject constructor(
    private val pixelSender: PixelSender,
) : Pixel {
    override fun fire(
        pixel: Pixel.PixelName,
        parameters: Map<String, String>,
        encodedParameters: Map<String, String>,
        type: PixelType,
    ) {
        fire(pixel.pixelName, parameters, encodedParameters, type)
    }

    @SuppressLint("CheckResult")
    override fun fire(
        pixelName: String,
        parameters: Map<String, String>,
        encodedParameters: Map<String, String>,
        type: PixelType,
    ) {
        pixelSender
            .sendPixel(pixelName, parameters, encodedParameters, type)
            .subscribeOn(Schedulers.io())
            .subscribe(
                { result ->
                    when (result) {
                        PIXEL_SENT -> Timber.v("Pixel sent: $pixelName with params: $parameters $encodedParameters")
                        PIXEL_IGNORED -> Timber.v("Pixel ignored: $pixelName with params: $parameters $encodedParameters")
                    }
                },
                {
                    Timber.w(
                        it,
                        "Pixel failed: $pixelName with params: $parameters $encodedParameters",
                    )
                },
            )
    }

    /**
     * Sends a pixel. If delivery fails, the pixel will be retried again in the future. As this
     * method stores the pixel to disk until successful delivery, check with privacy triage if the
     * pixel has additional parameters that they would want to validate.
     */
    override fun enqueueFire(
        pixel: Pixel.PixelName,
        parameters: Map<String, String>,
        encodedParameters: Map<String, String>,
    ) {
        enqueueFire(pixel.pixelName, parameters, encodedParameters)
    }

    @SuppressLint("CheckResult")
    /** See comment in {@link #enqueueFire(PixelName, Map<String, String>, Map<String, String>)}. */
    override fun enqueueFire(
        pixelName: String,
        parameters: Map<String, String>,
        encodedParameters: Map<String, String>,
    ) {
        pixelSender
            .enqueuePixel(pixelName, parameters, encodedParameters)
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    Timber.v(
                        "Pixel enqueued: $pixelName with params: $parameters $encodedParameters",
                    )
                },
                {
                    Timber.w(
                        it,
                        "Pixel failed: $pixelName with params: $parameters $encodedParameters",
                    )
                },
            )
    }
}
