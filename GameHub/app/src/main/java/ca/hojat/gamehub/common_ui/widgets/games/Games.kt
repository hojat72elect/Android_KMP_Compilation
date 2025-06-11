package ca.hojat.gamehub.common_ui.widgets.games

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import ca.hojat.gamehub.R
import ca.hojat.gamehub.common_ui.widgets.AnimatedContentContainer
import ca.hojat.gamehub.common_ui.widgets.FiniteUiState
import ca.hojat.gamehub.common_ui.widgets.GameNewsProgressIndicator
import ca.hojat.gamehub.common_ui.widgets.Info
import ca.hojat.gamehub.common_ui.widgets.RefreshableContent
import ca.hojat.gamehub.common_ui.theme.GameHubTheme

@Composable
fun Games(
    uiState: GamesUiState,
    modifier: Modifier = Modifier,
    onGameClicked: (GameUiModel) -> Unit,
    onBottomReached: () -> Unit,
) {
    AnimatedContentContainer(
        finiteUiState = uiState.finiteUiState,
        modifier = modifier,
    ) { finiteUiState ->
        when (finiteUiState) {
            FiniteUiState.EMPTY -> {
                EmptyState(
                    uiState = uiState,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
            FiniteUiState.LOADING -> {
                LoadingState(modifier = Modifier.align(Alignment.Center))
            }
            FiniteUiState.SUCCESS -> {
                SuccessState(
                    uiState = uiState,
                    modifier = Modifier.matchParentSize(),
                    onGameClicked = onGameClicked,
                    onBottomReached = onBottomReached,
                )
            }
        }
    }
}

@Composable
private fun LoadingState(modifier: Modifier) {
    GameNewsProgressIndicator(modifier = modifier)
}

@Composable
private fun EmptyState(
    uiState: GamesUiState,
    modifier: Modifier,
) {
    Info(
        icon = painterResource(uiState.infoIconId),
        title = uiState.infoTitle,
        modifier = modifier.padding(horizontal = GameHubTheme.spaces.spacing_7_0),
    )
}

@Composable
private fun SuccessState(
    uiState: GamesUiState,
    modifier: Modifier,
    onGameClicked: (GameUiModel) -> Unit,
    onBottomReached: () -> Unit,
) {
    RefreshableContent(
        isRefreshing = uiState.isRefreshing,
        modifier = modifier,
        isSwipeEnabled = false,
    ) {
        val games = uiState.games
        val lastIndex = games.lastIndex

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(GameHubTheme.spaces.spacing_3_5),
        ) {
            itemsIndexed(
                items = games,
                key = { _, game -> game.id }
            ) { index, game ->
                if (index == lastIndex) {
                    LaunchedEffect(lastIndex) {
                        onBottomReached()
                    }
                }

                Game(
                    game = game,
                    onClick = { onGameClicked(game) },
                )
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun GamesSuccessStatePreview() {
    val games = listOf(
        GameUiModel(
            id = 1,
            coverImageUrl = null,
            name = "Ghost of Tsushima: Director's Cut",
            releaseDate = "Aug 20, 2021 (2 months ago)",
            developerName = "Sucker Punch Productions",
            description = "Some very very very very very very very very very long description",
        ),
        GameUiModel(
            id = 2,
            coverImageUrl = null,
            name = "Forza Horizon 5",
            releaseDate = "Nov 09, 2021 (8 days ago)",
            developerName = "Playground Games",
            description = "Some very very very very very very very very very long description",
        ),
        GameUiModel(
            id = 3,
            coverImageUrl = null,
            name = "Outer Wilds: Echoes of the Eye",
            releaseDate = "Sep 28, 2021 (a month ago)",
            developerName = "Mobius Digital",
            description = "Some very very very very very very very very very long description",
        )
    )

    GameHubTheme {
        Games(
            uiState = GamesUiState(
                isLoading = false,
                infoIconId = 0,
                infoTitle = "",
                games = games,
            ),
            onGameClicked = {},
            onBottomReached = {},
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun GamesEmptyStatePreview() {
    GameHubTheme {
        Games(
            uiState = GamesUiState(
                isLoading = false,
                infoIconId = R.drawable.gamepad_variant_outline,
                infoTitle = "No Games\nNo Games",
                games = emptyList(),
            ),
            onGameClicked = {},
            onBottomReached = {},
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun GamesLoadingStatePreview() {
    GameHubTheme {
        Games(
            uiState = GamesUiState(
                isLoading = true,
                infoIconId = 0,
                infoTitle = "",
                games = emptyList(),
            ),
            onGameClicked = {},
            onBottomReached = {},
        )
    }
}
