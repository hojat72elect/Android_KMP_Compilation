package ca.hojat.gamehub.feature_discovery.widgets

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import ca.hojat.gamehub.R
import ca.hojat.gamehub.common_ui.CommandsHandler
import ca.hojat.gamehub.common_ui.NavBarColorHandler
import ca.hojat.gamehub.common_ui.RoutesHandler
import ca.hojat.gamehub.common_ui.base.events.Route
import ca.hojat.gamehub.common_ui.theme.GameHubTheme
import ca.hojat.gamehub.common_ui.widgets.RefreshableContent
import ca.hojat.gamehub.common_ui.widgets.categorypreview.GamesCategoryPreview
import ca.hojat.gamehub.common_ui.widgets.toolbars.Toolbar
import ca.hojat.gamehub.feature_discovery.DiscoverType
import ca.hojat.gamehub.feature_discovery.DiscoverViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Intentional delay to keep the swipe refresh visible
// because as soon as it is let go, it disappears instantaneously.
private const val SwipeRefreshIntentionalDelay = 300L

/**
 * The composable UI for Discover screen.
 */
@Composable
fun DiscoverScreen(
    modifier: Modifier,
    onRoute: (Route) -> Unit,
) {
    DiscoverScreen(
        viewModel = hiltViewModel(),
        modifier = modifier,
        onRoute = onRoute,
    )
}

@Composable
private fun DiscoverScreen(
    viewModel: DiscoverViewModel,
    modifier: Modifier,
    onRoute: (Route) -> Unit,
) {
    NavBarColorHandler()
    CommandsHandler(viewModel = viewModel)
    RoutesHandler(viewModel = viewModel, onRoute = onRoute)
    DiscoverScreen(
        items = viewModel.items.collectAsState().value,
        onCategoryMoreButtonClicked = viewModel::onCategoryMoreButtonClicked,
        onSearchButtonClicked = viewModel::onSearchButtonClicked,
        onCategoryGameClicked = viewModel::onCategoryItemClicked,
        onRefreshRequested = viewModel::onRefreshRequested,
        modifier = modifier,
    )
}

@Composable
private fun DiscoverScreen(
    items: List<DiscoverScreenUiModel>,
    onSearchButtonClicked: () -> Unit,
    onCategoryMoreButtonClicked: (category: String) -> Unit,
    onCategoryGameClicked: (DiscoverScreenItemData) -> Unit,
    onRefreshRequested: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            Toolbar(
                title = stringResource(R.string.games_discovery_toolbar_title),
                contentPadding = WindowInsets.statusBars
                    .only(WindowInsetsSides.Vertical + WindowInsetsSides.Horizontal)
                    .asPaddingValues(),
                firstButtonIcon = painterResource(R.drawable.magnify),
                onFirstButtonClick = onSearchButtonClicked,
            )
        },
    ) { paddingValues ->
        var isRefreshing by remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()

        RefreshableContent(
            isRefreshing = isRefreshing,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            onRefreshRequested = {
                isRefreshing = true

                coroutineScope.launch {
                    delay(SwipeRefreshIntentionalDelay)
                    onRefreshRequested()
                    isRefreshing = false
                }
            },
        ) {
            CategoryPreviewItems(
                items = items,
                onCategoryMoreButtonClicked = onCategoryMoreButtonClicked,
                onCategoryGameClicked = onCategoryGameClicked,
            )
        }
    }
}

@Composable
private fun CategoryPreviewItems(
    items: List<DiscoverScreenUiModel>,
    onCategoryMoreButtonClicked: (category: String) -> Unit,
    onCategoryGameClicked: (DiscoverScreenItemData) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(GameHubTheme.spaces.spacing_3_5),
    ) {
        items(items = items, key = DiscoverScreenUiModel::id) { item ->
            val categoryGames = remember(item.items) {
                item.items.mapToCategoryUiModels()
            }

            GamesCategoryPreview(
                title = item.title,
                isProgressBarVisible = item.isProgressBarVisible,
                games = categoryGames,
                onCategoryGameClicked = { onCategoryGameClicked(it.mapToDiscoveryUiModel()) },
                onCategoryMoreButtonClicked = { onCategoryMoreButtonClicked(item.categoryName) },
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun GamesDiscoverySuccessStatePreview() {
    val games = listOf(
        "Ghost of Tsushima: Director's Cut",
        "Outer Wilds: Echoes of the Eye",
        "Kena: Bridge of Spirits",
        "Forza Horizon 5",
    )
        .mapIndexed { index, gameTitle ->
            DiscoverScreenItemData(id = index, title = gameTitle, coverUrl = null)
        }

    val items = DiscoverType.values().map { category ->
        DiscoverScreenUiModel(
            id = category.id,
            categoryName = category.name,
            title = stringResource(category.titleId),
            isProgressBarVisible = true,
            items = games,
        )
    }

    GameHubTheme {
        DiscoverScreen(
            items = items,
            onSearchButtonClicked = {},
            onCategoryMoreButtonClicked = {},
            onCategoryGameClicked = {},
            onRefreshRequested = {},
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun GamesDiscoveryEmptyStatePreview() {
    val items = DiscoverType.values().map { category ->
        DiscoverScreenUiModel(
            id = category.id,
            categoryName = category.name,
            title = stringResource(category.titleId),
            isProgressBarVisible = true,
            items = emptyList(),
        )
    }

    GameHubTheme {
        DiscoverScreen(
            items = items,
            onSearchButtonClicked = {},
            onCategoryMoreButtonClicked = {},
            onCategoryGameClicked = {},
            onRefreshRequested = {},
        )
    }
}
