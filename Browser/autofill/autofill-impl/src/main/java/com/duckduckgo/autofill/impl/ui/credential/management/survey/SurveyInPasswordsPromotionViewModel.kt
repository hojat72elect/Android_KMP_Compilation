

package com.duckduckgo.autofill.impl.ui.credential.management.survey

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duckduckgo.anvil.annotations.ContributesViewModel
import com.duckduckgo.app.statistics.pixels.Pixel
import com.duckduckgo.autofill.impl.pixel.AutofillPixelNames
import com.duckduckgo.autofill.impl.ui.credential.management.survey.SurveyInPasswordsPromotionViewModel.Command.DismissSurvey
import com.duckduckgo.autofill.impl.ui.credential.management.survey.SurveyInPasswordsPromotionViewModel.Command.LaunchSurvey
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.ViewScope
import javax.inject.Inject
import kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@ContributesViewModel(ViewScope::class)
class SurveyInPasswordsPromotionViewModel @Inject constructor(
    private val autofillSurvey: AutofillSurvey,
    private val pixel: Pixel,
    private val dispatchers: DispatcherProvider,
) : ViewModel() {

    sealed interface Command {
        data class LaunchSurvey(val surveyUrl: String) : Command
        data object DismissSurvey : Command
    }

    private val command = Channel<Command>(1, DROP_OLDEST)
    internal fun commands(): Flow<Command> = command.receiveAsFlow()

    // want to ensure this pixel doesn't trigger repeatedly as it's scrolled in and out of the list
    private var promoDisplayedPixelSent = false

    fun onPromoShown() {
        if (!promoDisplayedPixelSent) {
            promoDisplayedPixelSent = true
            pixel.fire(AutofillPixelNames.AUTOFILL_SURVEY_AVAILABLE_PROMPT_DISPLAYED)
        }
    }

    fun onUserChoseToOpenSurvey(survey: SurveyDetails) {
        viewModelScope.launch(dispatchers.io()) {
            autofillSurvey.recordSurveyAsUsed(survey.id)
            command.send(LaunchSurvey(survey.url))
        }
    }

    fun onSurveyPromptDismissed(surveyId: String) {
        viewModelScope.launch(dispatchers.io()) {
            autofillSurvey.recordSurveyAsUsed(surveyId)
            command.send(DismissSurvey)
        }
    }
}
