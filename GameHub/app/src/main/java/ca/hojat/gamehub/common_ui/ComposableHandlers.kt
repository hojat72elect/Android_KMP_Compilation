package ca.hojat.gamehub.common_ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import ca.hojat.gamehub.core.extensions.showLongToast
import ca.hojat.gamehub.core.extensions.showShortToast
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import ca.hojat.gamehub.common_ui.base.BaseViewModel
import ca.hojat.gamehub.common_ui.base.events.Command
import ca.hojat.gamehub.common_ui.base.events.Route
import ca.hojat.gamehub.common_ui.base.events.GeneralCommand
import ca.hojat.gamehub.common_ui.theme.GameHubTheme
import ca.hojat.gamehub.common_ui.theme.navBar

@Composable
fun CommandsHandler(
    viewModel: BaseViewModel,
    onHandleCommand: ((Command) -> Unit)? = null,
) {
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.commandFlow.collect { command ->
            when (command) {
                is GeneralCommand.ShowShortToast -> context.showShortToast(command.message)
                is GeneralCommand.ShowLongToast -> context.showLongToast(command.message)
                else -> onHandleCommand?.invoke(command)
            }
        }
    }
}

@Composable
fun RoutesHandler(
    viewModel: BaseViewModel,
    onRoute: (Route) -> Unit,
) {
    LaunchedEffect(viewModel) {
        viewModel.routeFlow
            .collect(onRoute)
    }
}

@Composable
fun NavBarColorHandler() {
    val systemUiController = rememberSystemUiController()
    val navBarColor = GameHubTheme.colors.navBar

    LaunchedEffect(navBarColor) {
        systemUiController.setNavigationBarColor(navBarColor)
    }
}
