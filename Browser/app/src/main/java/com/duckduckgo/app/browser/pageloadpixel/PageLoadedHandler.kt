

package com.duckduckgo.app.browser.pageloadpixel

import com.duckduckgo.app.browser.UriString
import com.duckduckgo.app.browser.pageloadpixel.PageLoadedSites.Companion.sites
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.pixels.remoteconfig.OptimizeTrackerEvaluationRCWrapper
import com.duckduckgo.autoconsent.api.Autoconsent
import com.duckduckgo.browser.api.WebViewVersionProvider
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.common.utils.device.DeviceInfo
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface PageLoadedHandler {
    fun onPageLoaded(url: String, title: String?, start: Long, end: Long)
}

@ContributesBinding(AppScope::class)
class RealPageLoadedHandler @Inject constructor(
    private val deviceInfo: DeviceInfo,
    private val webViewVersionProvider: WebViewVersionProvider,
    private val pageLoadedPixelDao: PageLoadedPixelDao,
    @AppCoroutineScope private val appCoroutineScope: CoroutineScope,
    private val dispatcherProvider: DispatcherProvider,
    private val autoconsent: Autoconsent,
    private val optimizeTrackerEvaluationRCWrapper: OptimizeTrackerEvaluationRCWrapper,
) : PageLoadedHandler {

    override fun onPageLoaded(url: String, title: String?, start: Long, end: Long) {
        appCoroutineScope.launch(dispatcherProvider.io()) {
            if (sites.any { UriString.sameOrSubdomain(url, it) }) {
                pageLoadedPixelDao.add(
                    PageLoadedPixelEntity(
                        elapsedTime = end - start,
                        webviewVersion = webViewVersionProvider.getMajorVersion(),
                        appVersion = deviceInfo.appVersion,
                        cpmEnabled = autoconsent.isAutoconsentEnabled(),
                        trackerOptimizationEnabled = optimizeTrackerEvaluationRCWrapper.enabled,
                    ),
                )
            }
        }
    }
}
