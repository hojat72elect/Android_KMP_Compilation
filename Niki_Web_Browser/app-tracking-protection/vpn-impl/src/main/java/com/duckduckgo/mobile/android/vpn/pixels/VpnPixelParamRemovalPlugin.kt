

package com.duckduckgo.mobile.android.vpn.pixels

import com.duckduckgo.common.utils.plugins.pixel.PixelParamRemovalPlugin
import com.duckduckgo.common.utils.plugins.pixel.PixelParamRemovalPlugin.PixelParameter
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class VpnPixelParamRemovalPlugin @Inject constructor() : PixelParamRemovalPlugin {
    override fun names(): List<Pair<String, Set<PixelParameter>>> {
        return listOf(
            ATP_PIXEL_PREFIX to PixelParameter.removeAtb(),
            NETP_PIXEL_PREFIX to PixelParameter.removeAtb(),
            VPN_PIXEL_PREFIX to PixelParameter.removeAtb(),
            "m_atp_unprotected_apps_bucket_" to PixelParameter.removeAll(),
            "m_vpn_ev_moto_g_fix_" to PixelParameter.removeAll(),
            ATP_PPRO_UPSELL_PREFIX to PixelParameter.removeAtb(),
        )
    }

    companion object {
        private const val ATP_PIXEL_PREFIX = "m_atp_"
        private const val NETP_PIXEL_PREFIX = "m_netp_"
        private const val VPN_PIXEL_PREFIX = "m_vpn_"
        private const val ATP_PPRO_UPSELL_PREFIX = "m_atp_ppro-upsell"
    }
}
