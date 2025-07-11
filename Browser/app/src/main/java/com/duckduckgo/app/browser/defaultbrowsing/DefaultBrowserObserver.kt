

package com.duckduckgo.app.browser.defaultbrowsing

import androidx.lifecycle.LifecycleOwner
import com.duckduckgo.app.global.install.AppInstallStore
import com.duckduckgo.app.lifecycle.MainProcessLifecycleObserver
import com.duckduckgo.app.pixels.AppPixelName
import com.duckduckgo.app.statistics.pixels.Pixel
import com.duckduckgo.app.statistics.pixels.Pixel.PixelParameter

class DefaultBrowserObserver(
    private val defaultBrowserDetector: DefaultBrowserDetector,
    private val appInstallStore: AppInstallStore,
    private val pixel: Pixel,
) : MainProcessLifecycleObserver {

    override fun onResume(owner: LifecycleOwner) {
        val isDefaultBrowser = defaultBrowserDetector.isDefaultBrowser()
        if (appInstallStore.defaultBrowser != isDefaultBrowser) {
            appInstallStore.defaultBrowser = isDefaultBrowser
            when {
                isDefaultBrowser -> {
                    val params = mapOf(
                        PixelParameter.DEFAULT_BROWSER_SET_FROM_ONBOARDING to false.toString(),
                    )
                    pixel.fire(AppPixelName.DEFAULT_BROWSER_SET, params)
                }
                else -> pixel.fire(AppPixelName.DEFAULT_BROWSER_UNSET)
            }
        }
    }
}
