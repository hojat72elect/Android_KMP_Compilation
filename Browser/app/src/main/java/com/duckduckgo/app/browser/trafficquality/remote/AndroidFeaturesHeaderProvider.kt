

package com.duckduckgo.app.browser.trafficquality.remote

import com.duckduckgo.autoconsent.api.Autoconsent
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.mobile.android.app.tracking.AppTrackingProtection
import com.duckduckgo.networkprotection.api.NetworkProtectionState
import com.duckduckgo.privacy.config.api.Gpc
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import kotlinx.coroutines.runBlocking

interface AndroidFeaturesHeaderProvider {
    fun provide(config: TrafficQualityAppVersion): String?
}

@ContributesBinding(AppScope::class)
class RealAndroidFeaturesHeaderProvider @Inject constructor(
    private val autoconsent: Autoconsent,
    private val gpc: Gpc,
    private val appTrackingProtection: AppTrackingProtection,
    private val networkProtectionState: NetworkProtectionState,
) : AndroidFeaturesHeaderProvider {

    override fun provide(config: TrafficQualityAppVersion): String? {
        return logFeature(config)
    }

    private fun logFeature(versionConfig: TrafficQualityAppVersion): String? {
        val listOfFeatures = mutableListOf<String>()
        if (versionConfig.featuresLogged.cpm) {
            listOfFeatures.add(CPM_HEADER)
        }
        if (versionConfig.featuresLogged.gpc) {
            listOfFeatures.add(GPC_HEADER)
        }

        if (versionConfig.featuresLogged.appTP) {
            listOfFeatures.add(APP_TP_HEADER)
        }

        if (versionConfig.featuresLogged.netP) {
            listOfFeatures.add(NET_P_HEADER)
        }

        return if (listOfFeatures.isEmpty()) {
            null
        } else {
            val randomIndex = (0..<listOfFeatures.size).random()
            listOfFeatures[randomIndex].plus("=").plus(mapFeature(listOfFeatures[randomIndex]))
        }
    }

    private fun mapFeature(feature: String): String? {
        return runBlocking {
            when (feature) {
                CPM_HEADER -> {
                    autoconsent.isAutoconsentEnabled().toString()
                }

                GPC_HEADER -> {
                    gpc.isEnabled().toString()
                }

                APP_TP_HEADER -> {
                    appTrackingProtection.isEnabled().toString()
                }

                NET_P_HEADER -> {
                    networkProtectionState.isEnabled().toString()
                }

                else -> null
            }
        }
    }

    companion object {
        private const val CPM_HEADER = "cpm_enabled"
        private const val GPC_HEADER = "gpc_enabled"
        private const val APP_TP_HEADER = "atp_enabled"
        private const val NET_P_HEADER = "vpn_enabled"
    }
}
