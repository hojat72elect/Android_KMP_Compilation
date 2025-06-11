

package com.duckduckgo.mobile.android.vpn.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.mobile.android.vpn.VpnFeaturesRegistry
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject

@SingleInstanceIn(AppScope::class)
@ContributesBinding(AppScope::class)
class RealExternalVpnDetector @Inject constructor(
    private val context: Context,
    private val vpnFeaturesRegistry: VpnFeaturesRegistry,
) : ExternalVpnDetector {

    override suspend fun isExternalVpnDetected(): Boolean {
        // if we're the ones using the VPN, no VPN is detected
        if (vpnFeaturesRegistry.isAnyFeatureRunning()) return false

        return runCatching {
            val connectivityManager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetwork
            connectivityManager.getNetworkCapabilities(activeNetwork)?.hasTransport(NetworkCapabilities.TRANSPORT_VPN) ?: false
        }.getOrDefault(false)
    }
}
