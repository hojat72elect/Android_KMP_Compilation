

package com.duckduckgo.app.feedback.ui.negative.openended

import androidx.lifecycle.ViewModel
import com.duckduckgo.anvil.annotations.ContributesViewModel
import com.duckduckgo.app.feedback.ui.negative.FeedbackType.MainReason
import com.duckduckgo.app.feedback.ui.negative.FeedbackType.SubReason
import com.duckduckgo.common.utils.SingleLiveEvent
import com.duckduckgo.di.scopes.FragmentScope
import javax.inject.Inject

@ContributesViewModel(FragmentScope::class)
class ShareOpenEndedNegativeFeedbackViewModel @Inject constructor() : ViewModel() {

    val command: SingleLiveEvent<Command> = SingleLiveEvent()

    fun userSubmittingPositiveFeedback(feedback: String) {
        command.value = Command.ExitAndSubmitPositiveFeedback(feedback)
    }

    fun userSubmittingNegativeFeedback(
        mainReason: MainReason,
        subReason: SubReason?,
        openEndedComment: String,
    ) {
        command.value = Command.ExitAndSubmitNegativeFeedback(mainReason, subReason, openEndedComment)
    }

    sealed class Command {
        data class ExitAndSubmitNegativeFeedback(
            val mainReason: MainReason,
            val subReason: SubReason?,
            val feedback: String,
        ) : Command()

        data class ExitAndSubmitPositiveFeedback(val feedback: String) : Command()
        object Exit : Command()
    }
}
