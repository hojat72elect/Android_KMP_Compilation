

package com.duckduckgo.app.pixels.campaign

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.di.scopes.AppScope

@ContributesPluginPoint(AppScope::class)
interface CampaignPixelParamsAdditionPlugin {
    /**
     * @return extract the campaign name from the pixel query params
     */
    fun extractCampaign(queryParams: Map<String, String>): String?

    /**
     * @return list of pixels that would be considered for pixel param addition
     */
    fun names(): List<String>
}
