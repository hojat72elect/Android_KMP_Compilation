package ca.hojat.gamehub.common_ui.widgets.games

import androidx.compose.runtime.Immutable
import ca.hojat.gamehub.common_ui.widgets.FiniteUiState

@Immutable
data class GamesUiState(
    val isLoading: Boolean,
    val infoIconId: Int,
    val infoTitle: String,
    val games: List<GameUiModel>,
) {

    private val isInEmptyState: Boolean
        get() = (!isLoading && games.isEmpty())

    private val isInLoadingState: Boolean
        get() = (isLoading && games.isEmpty())

    private val isInSuccessState: Boolean
        get() = games.isNotEmpty()

    val isRefreshing: Boolean
        get() = (isLoading && games.isNotEmpty())

    val finiteUiState: FiniteUiState
        get() = when {
            isInEmptyState -> FiniteUiState.EMPTY
            isInLoadingState -> FiniteUiState.LOADING
            isInSuccessState -> FiniteUiState.SUCCESS
            else -> error("Unknown games UI state.")
        }

    fun toEmptyState(): GamesUiState {
        return copy(isLoading = false, games = emptyList())
    }

    fun toLoadingState(): GamesUiState {
        return copy(isLoading = true)
    }

    fun toSuccessState(games: List<GameUiModel>): GamesUiState {
        return copy(isLoading = false, games = games)
    }
}
