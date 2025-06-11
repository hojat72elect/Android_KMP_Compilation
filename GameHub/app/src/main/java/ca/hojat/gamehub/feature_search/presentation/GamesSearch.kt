package ca.hojat.gamehub.feature_search.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import ca.hojat.gamehub.R
import ca.hojat.gamehub.common_ui.CommandsHandler
import ca.hojat.gamehub.common_ui.NavBarColorHandler
import ca.hojat.gamehub.common_ui.OnLifecycleEvent
import ca.hojat.gamehub.common_ui.RoutesHandler
import ca.hojat.gamehub.common_ui.widgets.FiniteUiState
import ca.hojat.gamehub.common_ui.widgets.games.GameUiModel
import ca.hojat.gamehub.common_ui.widgets.games.Games
import ca.hojat.gamehub.common_ui.widgets.games.GamesUiState
import ca.hojat.gamehub.common_ui.widgets.toolbars.SearchToolbar
import ca.hojat.gamehub.common_ui.base.events.Route
import ca.hojat.gamehub.common_ui.theme.GameHubTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val KeyboardPopupIntentionalDelay = 300L

@Composable
fun GamesSearch(onRoute: (Route) -> Unit) {
    GamesSearch(
        viewModel = hiltViewModel(),
        onRoute = onRoute,
    )
}

@Composable
private fun GamesSearch(
    viewModel: GamesSearchViewModel,
    onRoute: (Route) -> Unit,
) {
    NavBarColorHandler()
    CommandsHandler(viewModel = viewModel)
    RoutesHandler(viewModel = viewModel, onRoute = onRoute)
    GamesSearch(
        uiState = viewModel.uiState.collectAsState().value,
        onSearchConfirmed = viewModel::onSearchConfirmed,
        onBackButtonClicked = viewModel::onToolbarBackButtonClicked,
        onClearButtonClicked = viewModel::onToolbarClearButtonClicked,
        onQueryChanged = viewModel::onQueryChanged,
        onGameClicked = viewModel::onGameClicked,
        onBottomReached = viewModel::onBottomReached,
    )
}

@Composable
private fun GamesSearch(
    uiState: GamesSearchUiState,
    onSearchConfirmed: (query: String) -> Unit,
    onBackButtonClicked: () -> Unit,
    onClearButtonClicked: () -> Unit,
    onQueryChanged: (query: String) -> Unit,
    onGameClicked: (GameUiModel) -> Unit,
    onBottomReached: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        modifier = Modifier.navigationBarsPadding(),
        topBar = {
            SearchToolbar(
                queryText = uiState.queryText,
                placeholderText = stringResource(R.string.games_search_toolbar_hint),
                contentPadding = WindowInsets.statusBars
                    .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Vertical)
                    .asPaddingValues(),
                focusRequester = focusRequester,
                onQueryChanged = onQueryChanged,
                onSearchConfirmed = { query ->
                    focusManager.clearFocus(force = true)
                    onSearchConfirmed(query)
                },
                onBackButtonClicked = onBackButtonClicked,
                onClearButtonClicked = onClearButtonClicked,
            )
        },
    ) { paddingValues ->
        Games(
            uiState = uiState.gamesUiState,
            modifier = Modifier.padding(paddingValues),
            onGameClicked = onGameClicked,
            onBottomReached = onBottomReached,
        )

        OnLifecycleEvent(
            onResume = {
                if (uiState.gamesUiState.finiteUiState == FiniteUiState.EMPTY) {
                    // On subsequent openings of this screen from the background,
                    // simply calling focusRequester.requestFocus() does not make
                    // the keyboard visible. The workaround is to add small delay
                    // and call keyboardController.show() as well.
                    coroutineScope.launch {
                        delay(KeyboardPopupIntentionalDelay)
                        focusRequester.requestFocus()
                        keyboardController?.show()
                    }
                }
            },
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun GamesSearchSuccessStatePreview() {
    GameHubTheme {
        GamesSearch(
            uiState = GamesSearchUiState(
                queryText = "God of War",
                gamesUiState = GamesUiState(
                    isLoading = false,
                    infoIconId = R.drawable.magnify,
                    infoTitle = "",
                    games = listOf(
                        GameUiModel(
                            id = 1,
                            coverImageUrl = null,
                            name = "God of War",
                            releaseDate = "Apr 20, 2018 (3 years ago)",
                            developerName = "SIE Santa Monica Studio",
                            description = "Very very very very very very very very very " +
                                    "very very very very very very very very long description",
                        ),
                        GameUiModel(
                            id = 2,
                            coverImageUrl = null,
                            name = "God of War II",
                            releaseDate = "Mar 13, 2007 (14 years ago)",
                            developerName = "SIE Santa Monica Studio",
                            description = "Very very very very very very very very very " +
                                    "very very very very very very very very long description",
                        ),
                        GameUiModel(
                            id = 3,
                            coverImageUrl = null,
                            name = "God of War II HD",
                            releaseDate = "Oct 02, 2010 (11 years ago)",
                            developerName = "Bluepoint Games",
                            description = "Very very very very very very very very very " +
                                    "very very very very very very very very long description",
                        ),
                    ),
                )
            ),
            onSearchConfirmed = {},
            onBackButtonClicked = {},
            onClearButtonClicked = {},
            onQueryChanged = {},
            onGameClicked = {},
            onBottomReached = {},
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun GamesSearchEmptyStatePreview() {
    GameHubTheme {
        GamesSearch(
            uiState = GamesSearchUiState(
                queryText = "God of War",
                gamesUiState = GamesUiState(
                    isLoading = false,
                    infoIconId = R.drawable.magnify,
                    infoTitle = "No games found for \n\"God of War\"",
                    games = emptyList(),
                )
            ),
            onSearchConfirmed = {},
            onBackButtonClicked = {},
            onClearButtonClicked = {},
            onQueryChanged = {},
            onGameClicked = {},
            onBottomReached = {},
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun GamesSearchLoadingStatePreview() {
    GameHubTheme {
        GamesSearch(
            uiState = GamesSearchUiState(
                queryText = "God of War",
                gamesUiState = GamesUiState(
                    isLoading = true,
                    infoIconId = R.drawable.magnify,
                    infoTitle = "",
                    games = emptyList(),
                )
            ),
            onSearchConfirmed = {},
            onBackButtonClicked = {},
            onClearButtonClicked = {},
            onQueryChanged = {},
            onGameClicked = {},
            onBottomReached = {},
        )
    }
}
