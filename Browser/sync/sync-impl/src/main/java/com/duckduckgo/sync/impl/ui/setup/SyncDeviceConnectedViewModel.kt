

package com.duckduckgo.sync.impl.ui.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duckduckgo.anvil.annotations.ContributesViewModel
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.sync.impl.ui.setup.SyncDeviceConnectedViewModel.Command.FinishSetupFlow
import com.duckduckgo.sync.impl.ui.setup.SyncDeviceConnectedViewModel.Command.LaunchSyncGetOnOtherPlatforms
import javax.inject.*
import kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@ContributesViewModel(ActivityScope::class)
class SyncDeviceConnectedViewModel @Inject constructor(
    private val dispatchers: DispatcherProvider,
) : ViewModel() {
    private val command = Channel<Command>(1, DROP_OLDEST)

    fun commands(): Flow<Command> = command.receiveAsFlow()
    data class ViewState(val deviceName: String)

    sealed class Command {
        data object FinishSetupFlow : Command()
        data object Error : Command()
        data object LaunchSyncGetOnOtherPlatforms : Command()
    }

    fun onDoneClicked() {
        viewModelScope.launch(dispatchers.main()) {
            command.send(FinishSetupFlow)
        }
    }

    fun onGetAppOnOtherDevicesClicked() {
        viewModelScope.launch(dispatchers.main()) {
            command.send(LaunchSyncGetOnOtherPlatforms)
        }
    }
}
