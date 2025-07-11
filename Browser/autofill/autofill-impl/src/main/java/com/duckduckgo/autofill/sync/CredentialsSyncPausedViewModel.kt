

package com.duckduckgo.autofill.sync

import android.annotation.SuppressLint
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duckduckgo.anvil.annotations.ContributesViewModel
import com.duckduckgo.autofill.impl.R
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.ViewScope
import com.duckduckgo.sync.api.engine.FeatureSyncError
import javax.inject.Inject
import kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@SuppressLint("NoLifecycleObserver") // does not subscribe to app lifecycle
@ContributesViewModel(ViewScope::class)
class CredentialsSyncPausedViewModel @Inject constructor(
    private val credentialsSyncStore: CredentialsSyncStore,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel(), DefaultLifecycleObserver {

    data class ViewState(
        val message: Int? = null,
    )

    sealed class Command {
        data object NavigateToCredentials : Command()
    }

    private val command = Channel<Command>(1, DROP_OLDEST)

    fun viewState(): Flow<ViewState> = credentialsSyncStore.isSyncPausedFlow()
        .map { syncPaused ->
            val message = when (credentialsSyncStore.syncPausedReason) {
                FeatureSyncError.COLLECTION_LIMIT_REACHED.name -> R.string.credentials_limit_warning
                FeatureSyncError.INVALID_REQUEST.name -> R.string.credentials_invalid_request_warning
                else -> null
            }
            ViewState(
                message = message,
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), ViewState())

    fun commands(): Flow<Command> = command.receiveAsFlow()

    fun onWarningActionClicked() {
        viewModelScope.launch {
            command.send(Command.NavigateToCredentials)
        }
    }
}
