

package com.duckduckgo.mobile.android.vpn

class FakeVpnFeaturesRegistry : VpnFeaturesRegistry {
    private val features = mutableSetOf<String>()

    override suspend fun registerFeature(feature: VpnFeature) {
        features.add(feature.featureName)
    }

    override suspend fun unregisterFeature(feature: VpnFeature) {
        features.remove(feature.featureName)
    }

    override suspend fun isFeatureRunning(feature: VpnFeature): Boolean {
        return features.contains(feature.featureName)
    }

    override suspend fun isFeatureRegistered(feature: VpnFeature): Boolean {
        return isFeatureRunning(feature)
    }

    override suspend fun isAnyFeatureRunning(): Boolean {
        return features.isNotEmpty()
    }

    override suspend fun isAnyFeatureRegistered(): Boolean {
        return isAnyFeatureRunning()
    }

    override suspend fun refreshFeature(feature: VpnFeature) {
        // no-op
    }

    override suspend fun getRegisteredFeatures(): List<VpnFeature> {
        return features.map { VpnFeature { it } }
    }
}
