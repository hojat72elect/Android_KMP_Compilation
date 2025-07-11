

package com.duckduckgo.app.pixels.remoteconfig

import com.duckduckgo.anvil.annotations.ContributesRemoteFeature
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.Toggle
import com.duckduckgo.feature.toggles.api.Toggle.DefaultFeatureValue

/**
 * This is the class that represents the browser feature flags
 */
@ContributesRemoteFeature(
    scope = AppScope::class,
    featureName = "androidBrowserConfig",
)
interface AndroidBrowserConfigFeature {

    /**
     * @return `true` when the remote config has the global "androidBrowserConfig" feature flag enabled
     * If the remote feature is not present defaults to `false`
     */
    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun self(): Toggle

    /**
     * @return `true` when the remote config has the global "collectFullWebViewVersion" androidBrowserConfig
     * sub-feature flag enabled
     * If the remote feature is not present defaults to `false`
     */
    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun collectFullWebViewVersion(): Toggle

    /**
     * @return `true` when the remote config has the global "screenLock" androidBrowserConfig
     * sub-feature flag enabled
     * If the remote feature is not present defaults to `false`
     */
    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun screenLock(): Toggle

    /**
     * @return `true` when the remote config has the global "optimizeTrackerEvaluationV2" androidBrowserConfig
     * sub-feature flag enabled
     * If the remote feature is not present defaults to `false`
     */
    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun optimizeTrackerEvaluationV2(): Toggle

    /**
     * @return `true` when the remote config has the global "errorPagePixel" androidBrowserConfig
     * sub-feature flag enabled
     * If the remote feature is not present defaults to `true`
     */
    @Toggle.DefaultValue(DefaultFeatureValue.TRUE)
    fun errorPagePixel(): Toggle

    /**
     * @return `true` when the remote config has the global "featuresRequestHeader" androidBrowserConfig
     * sub-feature flag enabled
     * If the remote feature is not present defaults to `false`
     */
    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun featuresRequestHeader(): Toggle

    /**
     * @return `true` when the remote config has the global "webLocalStorage" androidBrowserConfig
     * sub-feature flag enabled
     * If the remote feature is not present defaults to `false`
     */
    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun webLocalStorage(): Toggle

    /**
     * @return `true` when the remote config has the global "indexedDB" androidBrowserConfig
     * sub-feature flag enabled
     * If the remote feature is not present defaults to `false`
     */
    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun indexedDB(): Toggle

    /**
     * @return `true` when the remote config has the global "enableMaliciousSiteProtection" androidBrowserConfig
     * sub-feature flag enabled
     * If the remote feature is not present defaults to `false`
     */
    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun enableMaliciousSiteProtection(): Toggle

    /**
     * @return `true` when the remote config has the global "fireproofedWebLocalStorage" androidBrowserConfig
     * sub-feature flag enabled
     * If the remote feature is not present defaults to `false`
     */
    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun fireproofedWebLocalStorage(): Toggle

    /**
     * @return `true` when the remote config has the global "fireproofedIndexedDB" androidBrowserConfig
     * sub-feature flag enabled
     * If the remote feature is not present defaults to `false`
     */
    @Toggle.DefaultValue(DefaultFeatureValue.TRUE)
    fun fireproofedIndexedDB(): Toggle

    /**
     * @return `true` when the remote config has the global "httpError5xxPixel" androidBrowserConfig
     * sub-feature flag enabled
     * If the remote feature is not present defaults to `false`
     */
    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun httpError5xxPixel(): Toggle

    @Toggle.DefaultValue(DefaultFeatureValue.TRUE)
    fun glideSuspend(): Toggle

    /**
     * @return `true` when the remote config has the global "omnibarAnimation" androidBrowserConfig
     * sub-feature flag enabled
     * If the remote feature is not present defaults to `false`
     */
    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun omnibarAnimation(): Toggle

    @Toggle.DefaultValue(DefaultFeatureValue.TRUE)
    fun storeFaviconSuspend(): Toggle
}
