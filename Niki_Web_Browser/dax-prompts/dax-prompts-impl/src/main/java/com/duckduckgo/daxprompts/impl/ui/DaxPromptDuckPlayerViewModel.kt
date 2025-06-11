

package com.duckduckgo.daxprompts.impl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duckduckgo.anvil.annotations.ContributesViewModel
import com.duckduckgo.daxprompts.impl.ReactivateUsersExperiment
import com.duckduckgo.daxprompts.impl.repository.DaxPromptsRepository
import com.duckduckgo.di.scopes.ActivityScope
import javax.inject.Inject
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@ContributesViewModel(ActivityScope::class)
class DaxPromptDuckPlayerViewModel @Inject constructor(
    private val daxPromptsRepository: DaxPromptsRepository,
    private val reactivateUsersExperiment: ReactivateUsersExperiment,
) : ViewModel() {

    private val command = Channel<Command>(1, BufferOverflow.DROP_OLDEST)

    fun commands(): Flow<Command> {
        return command.receiveAsFlow()
    }

    fun onCloseButtonClicked() {
        viewModelScope.launch {
            command.send(Command.CloseScreen)
            reactivateUsersExperiment.fireCloseScreen()
        }
    }

    fun onPrimaryButtonClicked() {
        viewModelScope.launch {
            command.send(Command.TryDuckPlayer(DUCK_PLAYER_DEMO_URL))
            reactivateUsersExperiment.fireDuckPlayerClick()
        }
    }

    fun markDuckPlayerPromptAsShown() {
        viewModelScope.launch {
            daxPromptsRepository.setDaxPromptsShowDuckPlayer(false)
        }
    }

    sealed class Command {
        data object CloseScreen : Command()
        data class TryDuckPlayer(val url: String) : Command()
    }

    companion object {
        internal const val DUCK_PLAYER_DEMO_URL = "https://www.youtube.com/watch?v=yKWIA-Pys4c"
    }
}
