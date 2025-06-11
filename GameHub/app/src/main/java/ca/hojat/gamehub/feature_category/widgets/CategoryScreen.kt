package ca.hojat.gamehub.feature_category.widgets

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import ca.hojat.gamehub.common_ui.theme.GameHubTheme
import ca.hojat.gamehub.R
import ca.hojat.gamehub.common_ui.CommandsHandler
import ca.hojat.gamehub.common_ui.NavBarColorHandler
import ca.hojat.gamehub.common_ui.RoutesHandler
import ca.hojat.gamehub.common_ui.widgets.AnimatedContentContainer
import ca.hojat.gamehub.common_ui.widgets.FiniteUiState
import ca.hojat.gamehub.common_ui.widgets.GameCover
import ca.hojat.gamehub.common_ui.widgets.GameNewsProgressIndicator
import ca.hojat.gamehub.common_ui.widgets.Info
import ca.hojat.gamehub.common_ui.widgets.RefreshableContent
import ca.hojat.gamehub.common_ui.widgets.toolbars.Toolbar
import ca.hojat.gamehub.common_ui.base.events.Route
import ca.hojat.gamehub.feature_category.CategoryViewModel

@Composable
fun CategoryScreen(onRoute: (Route) -> Unit) {
    CategoryScreen(
        viewModel = hiltViewModel(),
        onRoute = onRoute,
    )
}

@Composable
private fun CategoryScreen(
    viewModel: CategoryViewModel,
    onRoute: (Route) -> Unit,
) {
    NavBarColorHandler()
    CommandsHandler(viewModel = viewModel)
    RoutesHandler(viewModel = viewModel, onRoute = onRoute)
    CategoryScreen(
        uiState = viewModel.uiState.collectAsState().value,
        onBackButtonClicked = viewModel::onToolbarLeftButtonClicked,
        onItemClicked = viewModel::onItemClicked,
        onBottomReached = viewModel::onBottomReached,
    )
}

@Composable
private fun CategoryScreen(
    uiState: CategoryUiState,
    onBackButtonClicked: () -> Unit,
    onItemClicked: (CategoryUiModel) -> Unit,
    onBottomReached: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.navigationBarsPadding(),
        topBar = {
            Toolbar(
                title = uiState.title,
                contentPadding = WindowInsets.statusBars
                    .only(WindowInsetsSides.Vertical + WindowInsetsSides.Horizontal)
                    .asPaddingValues(),
                backButtonIcon = painterResource(R.drawable.arrow_left),
                onBackButtonClick = onBackButtonClicked,
            )
        },
    ) { paddingValues ->
        AnimatedContentContainer(
            finiteUiState = uiState.finiteUiState,
            modifier = Modifier.padding(paddingValues),
        ) { finiteUiState ->
            when (finiteUiState) {
                FiniteUiState.EMPTY -> {
                    EmptyState(modifier = Modifier.align(Alignment.Center))
                }
                FiniteUiState.LOADING -> {
                    LoadingState(modifier = Modifier.align(Alignment.Center))
                }
                FiniteUiState.SUCCESS -> {
                    SuccessState(
                        uiState = uiState,
                        modifier = Modifier.matchParentSize(),
                        onItemClicked = onItemClicked,
                        onBottomReached = onBottomReached,
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingState(modifier: Modifier) {
    GameNewsProgressIndicator(modifier = modifier)
}

@Composable
private fun EmptyState(modifier: Modifier) {
    Info(
        icon = painterResource(R.drawable.gamepad_variant_outline),
        title = stringResource(R.string.games_category_info_view_title),
        modifier = modifier.padding(horizontal = GameHubTheme.spaces.spacing_7_5),
    )
}

@Composable
private fun SuccessState(
    uiState: CategoryUiState,
    modifier: Modifier,
    onItemClicked: (CategoryUiModel) -> Unit,
    onBottomReached: () -> Unit,
) {
    RefreshableContent(
        isRefreshing = uiState.isRefreshing,
        modifier = modifier,
        isSwipeEnabled = false,
    ) {
        VerticalGrid(
            games = uiState.items,
            onItemClicked = onItemClicked,
            onBottomReached = onBottomReached,
        )
    }
}

@Composable
private fun VerticalGrid(
    games: List<CategoryUiModel>,
    onItemClicked: (CategoryUiModel) -> Unit,
    onBottomReached: () -> Unit,
) {
    val gridConfig = rememberGridConfig()
    val gridItemSpacingInPx = gridConfig.itemSpacingInPx
    val gridSpanCount = gridConfig.spanCount
    val lastIndex = games.lastIndex

    LazyVerticalGrid(columns = GridCells.Fixed(gridConfig.spanCount)) {
        itemsIndexed(items = games) { index, game ->
            if (index == lastIndex) {
                LaunchedEffect(lastIndex) {
                    onBottomReached()
                }
            }

            val column = (index % gridConfig.spanCount)
            val paddingValues = with(LocalDensity.current) {
                PaddingValues(
                    top = (if (index < gridSpanCount) gridItemSpacingInPx else 0f).toDp(),
                    bottom = gridItemSpacingInPx.toDp(),
                    start = (gridItemSpacingInPx - (column * gridItemSpacingInPx / gridSpanCount)).toDp(),
                    end = ((column + 1) * gridItemSpacingInPx / gridSpanCount).toDp(),
                )
            }

            GameCover(
                title = game.title,
                imageUrl = game.coverUrl,
                modifier = Modifier
                    .size(
                        width = gridConfig.itemWidthInDp,
                        height = gridConfig.itemHeightInDp
                    )
                    .padding(paddingValues),
                hasRoundedShape = false,
                onCoverClicked = { onItemClicked(game) },
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CategorySuccessStatePreview() {
    val items = buildList {
        repeat(15) { index ->
            add(
                CategoryUiModel(
                    id = index + 1,
                    title = "Popular Game",
                    coverUrl = null,
                )
            )
        }
    }

    GameHubTheme {
        CategoryScreen(
            uiState = CategoryUiState(
                isLoading = false,
                title = "Popular",
                items = items,
            ),
            onBackButtonClicked = {},
            onItemClicked = {},
            onBottomReached = {},
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CategoryEmptyStatePreview() {
    GameHubTheme {
        CategoryScreen(
            uiState = CategoryUiState(
                isLoading = false,
                title = "Popular",
                items = emptyList(),
            ),
            onBackButtonClicked = {},
            onItemClicked = {},
            onBottomReached = {},
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CategoryLoadingStatePreview() {
    GameHubTheme {
        CategoryScreen(
            uiState = CategoryUiState(
                isLoading = true,
                title = "Popular",
                items = emptyList(),
            ),
            onBackButtonClicked = {},
            onItemClicked = {},
            onBottomReached = {},
        )
    }
}
