

package com.duckduckgo.app.feedback.ui.initial

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.duckduckgo.anvil.annotations.ContributesViewModel
import com.duckduckgo.app.feedback.ui.common.ViewState
import com.duckduckgo.di.scopes.FragmentScope
import javax.inject.Inject

@ContributesViewModel(FragmentScope::class)
class InitialFeedbackFragmentViewModel @Inject constructor() : ViewModel() {

    val viewState: MutableLiveData<ViewState> = MutableLiveData()
    val command: MutableLiveData<Command> = MutableLiveData()

    fun onPositiveFeedback() {
        command.value = Command.PositiveFeedbackSelected
    }

    fun onNegativeFeedback() {
        command.value = Command.NegativeFeedbackSelected
    }

    sealed class Command {
        object PositiveFeedbackSelected : Command()
        object NegativeFeedbackSelected : Command()
        object UserCancelled : Command()
    }
}
