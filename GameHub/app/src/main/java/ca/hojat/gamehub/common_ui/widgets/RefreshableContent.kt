package ca.hojat.gamehub.common_ui.widgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import ca.hojat.gamehub.common_ui.theme.GameHubTheme

@Composable
fun RefreshableContent(
    isRefreshing: Boolean,
    modifier: Modifier = Modifier,
    isSwipeEnabled: Boolean = true,
    onRefreshRequested: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = onRefreshRequested ?: {},
        modifier = modifier,
        swipeEnabled = isSwipeEnabled,
        indicator = { state, refreshTrigger ->
            SwipeRefreshIndicator(
                state = state,
                refreshTriggerDistance = refreshTrigger,
                contentColor = GameHubTheme.colors.secondary,
                refreshingOffset = GameHubTheme.spaces.spacing_6_0,
            )
        },
        content = content,
    )
}
