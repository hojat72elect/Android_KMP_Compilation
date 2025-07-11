

package com.duckduckgo.mobile.android.vpn

/**
 * Use this class to register features that required VPN access.
 *
 * Registering a feature will cause the VPN to be enabled.
 * Unregistering a feature will cause the VPN to be disabled IF no other feature is registered.
 */
interface VpnFeaturesRegistry {

    /**
     * Call this method to register a feature that requires VPN access.
     * If the VPN is not enabled, it will be enabled.
     */
    suspend fun registerFeature(feature: VpnFeature)

    /**
     * Call this method to unregister a feature that requires VPN access.
     * If the VPN will be disabled if and only if this is the last registered feature.
     */
    suspend fun unregisterFeature(feature: VpnFeature)

    /**
     * @return `true` if this feature is registered and the VPN is running, `false` otherwise.
     */
    suspend fun isFeatureRunning(feature: VpnFeature): Boolean

    /**
     * @return `true` if this feature is registered, `false` otherwise.
     */
    suspend fun isFeatureRegistered(feature: VpnFeature): Boolean

    /**
     * @return returns `true` if there's any feature currently using the VPN and the VPN is running, `false` otherwise
     */
    suspend fun isAnyFeatureRunning(): Boolean

    /**
     * @return returns `true` if there's any feature currently using the VPN, `false` otherwise
     */
    suspend fun isAnyFeatureRegistered(): Boolean

    /**
     * Refreshing the feature will cause the VPN to be stopped/restarted if it is enabled and the feature is already registered.
     */
    suspend fun refreshFeature(feature: VpnFeature)

    suspend fun getRegisteredFeatures(): List<VpnFeature>
}

interface VpnFeature {
    val featureName: String
}

/**
 * Fake constructor for [VpnFeature] from the passed in [block] lambda
 * instead of using the anonymous `object : VpnFeature` syntax.
 *
 * Usage:
 *
 * ```kotlin
 * val name = VpnFeature {
 *
 * }
 * ```
 */
fun VpnFeature(block: () -> String): VpnFeature = object : VpnFeature {
    override val featureName: String = block()
}
