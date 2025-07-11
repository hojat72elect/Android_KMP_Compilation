

package com.duckduckgo.app.browser.remotemessage

import com.duckduckgo.app.browser.commands.Command
import com.duckduckgo.app.browser.commands.Command.*
import com.duckduckgo.app.browser.newtab.NewTabLegacyPageViewModel
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.remote.messaging.api.Action
import com.duckduckgo.remote.messaging.api.Action.*
import com.duckduckgo.survey.api.SurveyParameterManager
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface CommandActionMapper {
    suspend fun asNewTabCommand(action: Action): NewTabLegacyPageViewModel.Command
}

@ContributesBinding(ActivityScope::class)
class RealCommandActionMapper @Inject constructor(
    private val surveyParameterManager: SurveyParameterManager,
) : CommandActionMapper {
    override suspend fun asNewTabCommand(action: Action): NewTabLegacyPageViewModel.Command {
        return when (action) {
            is Dismiss -> NewTabLegacyPageViewModel.Command.DismissMessage
            is PlayStore -> NewTabLegacyPageViewModel.Command.LaunchPlayStore(action.value)
            is Url -> NewTabLegacyPageViewModel.Command.SubmitUrl(action.value)
            is DefaultBrowser -> NewTabLegacyPageViewModel.Command.LaunchDefaultBrowser
            is AppTpOnboarding -> NewTabLegacyPageViewModel.Command.LaunchAppTPOnboarding
            is Share -> NewTabLegacyPageViewModel.Command.SharePromoLinkRMF(action.value, action.title)
            is Navigation -> { NewTabLegacyPageViewModel.Command.LaunchScreen(action.value, action.additionalParameters?.get("payload").orEmpty()) }
            is Survey -> {
                val queryParams = action.additionalParameters?.get("queryParams")?.split(";") ?: emptyList()
                NewTabLegacyPageViewModel.Command.SubmitUrl(surveyParameterManager.buildSurveyUrl(action.value, queryParams))
            }
        }
    }
}
