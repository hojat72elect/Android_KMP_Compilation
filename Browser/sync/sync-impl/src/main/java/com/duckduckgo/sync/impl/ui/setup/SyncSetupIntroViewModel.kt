

package com.duckduckgo.sync.impl.ui.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duckduckgo.anvil.annotations.ContributesViewModel
import com.duckduckgo.di.scopes.FragmentScope
import com.duckduckgo.sync.impl.ui.setup.SetupAccountActivity.Companion.Screen
import com.duckduckgo.sync.impl.ui.setup.SetupAccountActivity.Companion.Screen.SYNC_INTRO
import com.duckduckgo.sync.impl.ui.setup.SyncSetupIntroViewModel.Command.AbortFlow
import com.duckduckgo.sync.impl.ui.setup.SyncSetupIntroViewModel.Command.RecoverDataFlow
import com.duckduckgo.sync.impl.ui.setup.SyncSetupIntroViewModel.Command.StartSetupFlow
import com.duckduckgo.sync.impl.ui.setup.SyncSetupIntroViewModel.ViewMode.CreateAccountIntro
import com.duckduckgo.sync.impl.ui.setup.SyncSetupIntroViewModel.ViewMode.RecoverAccountIntro
import javax.inject.*
import kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@ContributesViewModel(FragmentScope::class)
class SyncSetupIntroViewModel @Inject constructor() : ViewModel() {

    private val command = Channel<Command>(1, DROP_OLDEST)

    private val viewState = MutableStateFlow(ViewState())
    fun viewState(screen: Screen): Flow<ViewState> = viewState.onStart {
        val viewMode = when (screen) {
            SYNC_INTRO -> CreateAccountIntro
            else -> RecoverAccountIntro
        }
        viewState.emit(ViewState(viewMode))
    }

    fun commands(): Flow<Command> = command.receiveAsFlow()

    sealed class Command {
        object StartSetupFlow : Command()
        object RecoverDataFlow : Command()
        object AbortFlow : Command()
    }

    data class ViewState(
        val viewMode: ViewMode = CreateAccountIntro,
    )

    sealed class ViewMode {
        object CreateAccountIntro : ViewMode()
        object RecoverAccountIntro : ViewMode()
    }

    fun onTurnSyncOnClicked() {
        viewModelScope.launch {
            command.send(StartSetupFlow)
        }
    }

    fun onStartRecoverDataClicked() {
        viewModelScope.launch {
            command.send(RecoverDataFlow)
        }
    }

    fun onAbortClicked() {
        viewModelScope.launch {
            command.send(AbortFlow)
        }
    }
}
