

package com.duckduckgo.mobile.android.app.tracking

import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.mobile.android.vpn.AppTpVpnFeature
import com.duckduckgo.mobile.android.vpn.VpnFeaturesRegistry
import com.duckduckgo.mobile.android.vpn.apps.TrackingProtectionAppsRepository
import com.duckduckgo.mobile.android.vpn.ui.onboarding.VpnStore
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@ContributesBinding(AppScope::class)
@SingleInstanceIn(AppScope::class)
class RealAppTrackingProtection @Inject constructor(
    private val vpnStore: VpnStore,
    private val vpnFeaturesRegistry: VpnFeaturesRegistry,
    @AppCoroutineScope private val coroutineScope: CoroutineScope,
    private val dispatcherProvider: DispatcherProvider,
    private val trackingProtectionAppsRepository: TrackingProtectionAppsRepository,
) : AppTrackingProtection {
    override suspend fun isOnboarded(): Boolean = withContext(dispatcherProvider.io()) {
        return@withContext vpnStore.didShowOnboarding()
    }

    override suspend fun isEnabled(): Boolean {
        return vpnFeaturesRegistry.isFeatureRegistered(AppTpVpnFeature.APPTP_VPN)
    }

    override suspend fun isRunning(): Boolean {
        return vpnFeaturesRegistry.isFeatureRunning(AppTpVpnFeature.APPTP_VPN)
    }

    override fun restart() {
        coroutineScope.launch(dispatcherProvider.io()) {
            vpnFeaturesRegistry.refreshFeature(AppTpVpnFeature.APPTP_VPN)
        }
    }

    override fun stop() {
        coroutineScope.launch {
            vpnFeaturesRegistry.unregisterFeature(AppTpVpnFeature.APPTP_VPN)
        }
    }

    override suspend fun getExcludedApps(): List<String> = withContext(dispatcherProvider.io()) {
        trackingProtectionAppsRepository.getExclusionAppsList()
    }
}
