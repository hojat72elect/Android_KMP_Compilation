package ca.hojat.gamehub.common_ui.base

import androidx.lifecycle.ViewModel
import ca.hojat.gamehub.common_ui.base.events.Command
import ca.hojat.gamehub.common_ui.base.events.Route
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

abstract class BaseViewModel : ViewModel() {

    private val _commandChannel = Channel<Command>(Channel.BUFFERED)
    private val _routeChannel = Channel<Route>(Channel.BUFFERED)

    val commandFlow: Flow<Command> = _commandChannel.receiveAsFlow()
    val routeFlow: Flow<Route> = _routeChannel.receiveAsFlow()

    protected fun dispatchCommand(command: Command) {
        _commandChannel.trySend(command)
    }

    protected fun route(route: Route) {
        _routeChannel.trySend(route)
    }
}
