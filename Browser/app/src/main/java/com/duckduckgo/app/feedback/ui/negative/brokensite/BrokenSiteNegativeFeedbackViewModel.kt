

package com.duckduckgo.app.feedback.ui.negative.brokensite

import androidx.lifecycle.ViewModel
import com.duckduckgo.anvil.annotations.ContributesViewModel
import com.duckduckgo.common.utils.SingleLiveEvent
import com.duckduckgo.di.scopes.FragmentScope
import javax.inject.Inject

@ContributesViewModel(FragmentScope::class)
class BrokenSiteNegativeFeedbackViewModel @Inject constructor() : ViewModel() {

    val command: SingleLiveEvent<Command> = SingleLiveEvent()

    fun userSubmittingFeedback(
        feedback: String,
        brokenSite: String?,
    ) {
        command.value = Command.ExitAndSubmitFeedback(feedback, brokenSite)
    }

    sealed class Command {
        data class ExitAndSubmitFeedback(
            val feedback: String,
            val brokenSite: String?,
        ) : Command()

        object Exit : Command()
    }
}
