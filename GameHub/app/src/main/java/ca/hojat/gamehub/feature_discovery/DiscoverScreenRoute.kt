@file:Suppress("MatchingDeclarationName")

package ca.hojat.gamehub.feature_discovery

import ca.hojat.gamehub.common_ui.base.events.Route

sealed class DiscoverScreenRoute : Route {
    object Search : DiscoverScreenRoute()
    data class Category(val category: String) : DiscoverScreenRoute()
    data class Info(val itemId: Int) : DiscoverScreenRoute()
}
