

package com.duckduckgo.app.widget.ui

import androidx.lifecycle.ViewModel
import com.duckduckgo.anvil.annotations.ContributesViewModel
import com.duckduckgo.app.widget.ui.AddWidgetInstructionsViewModel.Command.Close
import com.duckduckgo.app.widget.ui.AddWidgetInstructionsViewModel.Command.ShowHome
import com.duckduckgo.common.utils.SingleLiveEvent
import com.duckduckgo.di.scopes.ActivityScope
import javax.inject.Inject

@ContributesViewModel(ActivityScope::class)
class AddWidgetInstructionsViewModel @Inject constructor() : ViewModel() {

    sealed class Command {
        object ShowHome : Command()
        object Close : Command()
    }

    val command: SingleLiveEvent<Command> = SingleLiveEvent()

    fun onShowHomePressed() {
        command.value = ShowHome
    }

    fun onClosePressed() {
        command.value = Close
    }
}
