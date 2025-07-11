

package com.duckduckgo.app.browser.uriloaded

import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.di.IsMainProcess
import com.duckduckgo.app.pixels.AppPixelName
import com.duckduckgo.app.statistics.pixels.Pixel
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacy.config.api.PrivacyConfigCallbackPlugin
import com.squareup.anvil.annotations.ContributesBinding
import com.squareup.anvil.annotations.ContributesMultibinding
import dagger.SingleInstanceIn
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface UriLoadedManager {
    fun sendUriLoadedPixel()
}

@ContributesBinding(
    scope = AppScope::class,
    boundType = UriLoadedManager::class,
)
@ContributesMultibinding(
    scope = AppScope::class,
    boundType = PrivacyConfigCallbackPlugin::class,
)
@SingleInstanceIn(AppScope::class)
class DuckDuckGoUriLoadedManager @Inject constructor(
    private val pixel: Pixel,
    private val uriLoadedPixelFeature: UriLoadedPixelFeature,
    @AppCoroutineScope private val appCoroutineScope: CoroutineScope,
    private val dispatcherProvider: DispatcherProvider,
    @IsMainProcess isMainProcess: Boolean,
) : UriLoadedManager, PrivacyConfigCallbackPlugin {

    private var shouldSendUriLoadedPixel: Boolean = false

    init {
        appCoroutineScope.launch(dispatcherProvider.io()) {
            if (isMainProcess) {
                loadToMemory()
            }
        }
    }

    override fun sendUriLoadedPixel() {
        if (shouldSendUriLoadedPixel) {
            pixel.fire(AppPixelName.URI_LOADED)
        }
    }

    override fun onPrivacyConfigDownloaded() {
        appCoroutineScope.launch(dispatcherProvider.io()) {
            loadToMemory()
        }
    }

    private fun loadToMemory() {
        shouldSendUriLoadedPixel = uriLoadedPixelFeature.self().isEnabled()
    }
}
