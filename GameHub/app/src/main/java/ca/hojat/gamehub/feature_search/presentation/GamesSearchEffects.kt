package ca.hojat.gamehub.feature_search.presentation

import ca.hojat.gamehub.common_ui.base.events.Route

sealed class GamesSearchRoute : Route {
    data class Info(val gameId: Int) : GamesSearchRoute()
    object Back : GamesSearchRoute()
}
