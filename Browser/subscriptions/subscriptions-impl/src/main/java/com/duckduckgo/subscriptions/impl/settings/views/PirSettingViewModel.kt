

package com.duckduckgo.subscriptions.impl.settings.views

import android.annotation.SuppressLint
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duckduckgo.anvil.annotations.ContributesViewModel
import com.duckduckgo.di.scopes.ViewScope
import com.duckduckgo.subscriptions.api.Product.PIR
import com.duckduckgo.subscriptions.api.SubscriptionStatus
import com.duckduckgo.subscriptions.api.Subscriptions
import com.duckduckgo.subscriptions.impl.pixels.SubscriptionPixelSender
import com.duckduckgo.subscriptions.impl.settings.views.PirSettingViewModel.Command.OpenPir
import com.duckduckgo.subscriptions.impl.settings.views.PirSettingViewModel.ViewState.PirState
import javax.inject.Inject
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@SuppressLint("NoLifecycleObserver") // we don't observe app lifecycle
@ContributesViewModel(ViewScope::class)
class PirSettingViewModel @Inject constructor(
    private val pixelSender: SubscriptionPixelSender,
    private val subscriptions: Subscriptions,
) : ViewModel(), DefaultLifecycleObserver {

    sealed class Command {
        data object OpenPir : Command()
    }

    private val command = Channel<Command>(1, BufferOverflow.DROP_OLDEST)
    internal fun commands(): Flow<Command> = command.receiveAsFlow()
    data class ViewState(val pirState: PirState = PirState.Hidden) {

        sealed class PirState {

            data object Hidden : PirState()
            data object Enabled : PirState()
            data object Disabled : PirState()
        }
    }

    private val _viewState = MutableStateFlow(ViewState())
    val viewState = _viewState.asStateFlow()

    fun onPir() {
        pixelSender.reportAppSettingsPirClick()
        sendCommand(OpenPir)
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        subscriptions.getEntitlementStatus().map { entitledProducts -> entitledProducts.contains(PIR) }
            .onEach { hasValidEntitlement ->

                val subscriptionStatus = subscriptions.getSubscriptionStatus()

                val pirState = getPirState(hasValidEntitlement, subscriptionStatus)

                _viewState.update { it.copy(pirState = pirState) }
            }
            .launchIn(viewModelScope)
    }

    private suspend fun getPirState(
        hasValidEntitlement: Boolean,
        subscriptionStatus: SubscriptionStatus,
    ): PirState {
        return when (subscriptionStatus) {
            SubscriptionStatus.UNKNOWN -> PirState.Hidden

            SubscriptionStatus.INACTIVE,
            SubscriptionStatus.EXPIRED,
            SubscriptionStatus.WAITING,
            -> {
                if (isPirAvailable()) {
                    PirState.Disabled
                } else {
                    PirState.Hidden
                }
            }

            SubscriptionStatus.AUTO_RENEWABLE,
            SubscriptionStatus.NOT_AUTO_RENEWABLE,
            SubscriptionStatus.GRACE_PERIOD,
            -> {
                if (hasValidEntitlement) {
                    PirState.Enabled
                } else {
                    PirState.Hidden
                }
            }
        }
    }

    private suspend fun isPirAvailable(): Boolean {
        return subscriptions.getAvailableProducts().contains(PIR)
    }

    private fun sendCommand(newCommand: Command) {
        viewModelScope.launch {
            command.send(newCommand)
        }
    }
}
