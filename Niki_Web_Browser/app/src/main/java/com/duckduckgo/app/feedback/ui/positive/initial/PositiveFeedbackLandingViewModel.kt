

package com.duckduckgo.app.feedback.ui.positive.initial

import androidx.lifecycle.ViewModel
import com.duckduckgo.anvil.annotations.ContributesViewModel
import com.duckduckgo.common.utils.SingleLiveEvent
import com.duckduckgo.di.scopes.FragmentScope
import javax.inject.Inject

@ContributesViewModel(FragmentScope::class)
class PositiveFeedbackLandingViewModel @Inject constructor() : ViewModel() {

    val command: SingleLiveEvent<Command> = SingleLiveEvent()

    fun userSelectedToRateApp() {
        command.value = Command.LaunchPlayStore
    }

    fun userSelectedToProvideFeedbackDetails() {
        command.value = Command.LaunchShareFeedbackPage
    }

    fun userFinishedGivingPositiveFeedback() {
        command.value = Command.Exit
    }
}

data class ViewState(val canShowRatingButton: Boolean)

sealed class Command {
    object LaunchPlayStore : Command()
    object Exit : Command()
    object LaunchShareFeedbackPage : Command()
}
