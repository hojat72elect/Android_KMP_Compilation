

package com.duckduckgo.app.pixels.campaign

import com.duckduckgo.app.statistics.pixels.Pixel
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class RmfCampaignPixelParamsAdditionPlugin @Inject constructor() : CampaignPixelParamsAdditionPlugin {
    override fun extractCampaign(queryParams: Map<String, String>): String? {
        return queryParams[Pixel.PixelParameter.MESSAGE_SHOWN]
    }

    override fun names(): List<String> = listOf(
        "m_remote_message_shown",
        "m_remote_message_shown_unique",
        "m_remote_message_dismissed",
        "m_remote_message_primary_action_clicked",
        "m_remote_message_secondary_action_clicked",
        "m_remote_message_action_clicked",
        "m_remote_message_share",
    )
}
