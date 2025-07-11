

package com.duckduckgo.mobile.android.vpn.pixels

import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.statistics.api.AtbLifecyclePlugin
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.mobile.android.vpn.AppTpVpnFeature
import com.duckduckgo.mobile.android.vpn.VpnFeaturesRegistry
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ContributesMultibinding(AppScope::class)
class DeviceShieldRetentionPixelSender @Inject constructor(
    private val deviceShieldPixels: DeviceShieldPixels,
    private val vpnFeaturesRegistry: VpnFeaturesRegistry,
    @AppCoroutineScope private val coroutineScope: CoroutineScope,
    private val dispatcherProvider: DispatcherProvider,
) : AtbLifecyclePlugin {

    override fun onSearchRetentionAtbRefreshed(oldAtb: String, newAtb: String) {
        coroutineScope.launch(dispatcherProvider.io()) {
            if (vpnFeaturesRegistry.isFeatureRunning(AppTpVpnFeature.APPTP_VPN)) {
                deviceShieldPixels.deviceShieldEnabledOnSearch()
            } else {
                deviceShieldPixels.deviceShieldDisabledOnSearch()
            }
        }
    }

    override fun onAppRetentionAtbRefreshed(oldAtb: String, newAtb: String) {
        coroutineScope.launch(dispatcherProvider.io()) {
            if (vpnFeaturesRegistry.isFeatureRunning(AppTpVpnFeature.APPTP_VPN)) {
                deviceShieldPixels.deviceShieldEnabledOnAppLaunch()
            } else {
                deviceShieldPixels.deviceShieldDisabledOnAppLaunch()
            }
        }
    }
}
