

package com.duckduckgo.app.browser.trafficquality

import com.duckduckgo.app.browser.DuckDuckGoUrlDetector
import com.duckduckgo.app.browser.trafficquality.Result.Allowed
import com.duckduckgo.app.browser.trafficquality.Result.NotAllowed
import com.duckduckgo.app.browser.trafficquality.remote.AndroidFeaturesHeaderProvider
import com.duckduckgo.app.pixels.remoteconfig.AndroidBrowserConfigFeature
import com.duckduckgo.common.utils.plugins.headers.CustomHeadersProvider.CustomHeadersPlugin
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(scope = AppScope::class)
class AndroidFeaturesHeaderPlugin @Inject constructor(
    private val duckDuckGoUrlDetector: DuckDuckGoUrlDetector,
    private val customHeaderAllowedChecker: CustomHeaderAllowedChecker,
    private val androidBrowserConfigFeature: AndroidBrowserConfigFeature,
    private val androidFeaturesHeaderProvider: AndroidFeaturesHeaderProvider,
    private val appVersionProvider: AppVersionHeaderProvider,
) : CustomHeadersPlugin {

    override fun getHeaders(url: String): Map<String, String> {
        if (isFeatureEnabled() && duckDuckGoUrlDetector.isDuckDuckGoQueryUrl(url)) {
            return when (val result = customHeaderAllowedChecker.isAllowed()) {
                is Allowed -> {
                    val headers = mutableMapOf<String, String>()
                    androidFeaturesHeaderProvider.provide(result.config)?.let { headerValue ->
                        headers.put(X_DUCKDUCKGO_ANDROID_HEADER, headerValue)
                    }
                    appVersionProvider.provide(isStub = false).let { headerValue ->
                        headers.put(X_DUCKDUCKGO_ANDROID_APP_VERSION_HEADER, headerValue)
                    }
                    headers
                }

                NotAllowed -> {
                    mapOf(X_DUCKDUCKGO_ANDROID_APP_VERSION_HEADER to appVersionProvider.provide(isStub = true))
                }
            }
        } else {
            return emptyMap()
        }
    }

    private fun isFeatureEnabled(): Boolean {
        return androidBrowserConfigFeature.self().isEnabled() &&
            androidBrowserConfigFeature.featuresRequestHeader().isEnabled()
    }

    companion object {
        internal const val X_DUCKDUCKGO_ANDROID_HEADER = "x-duckduckgo-android"
        internal const val X_DUCKDUCKGO_ANDROID_APP_VERSION_HEADER = "x-duckduckgo-android-version"
    }
}
