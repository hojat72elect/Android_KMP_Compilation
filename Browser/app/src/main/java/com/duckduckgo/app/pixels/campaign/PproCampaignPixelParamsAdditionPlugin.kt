

package com.duckduckgo.app.pixels.campaign

import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class PproCampaignPixelParamsAdditionPlugin @Inject constructor() : CampaignPixelParamsAdditionPlugin {

    override fun extractCampaign(queryParams: Map<String, String>): String? {
        return queryParams["origin"]
    }

    override fun names(): List<String> = listOf("m_subscribe_android")
}
