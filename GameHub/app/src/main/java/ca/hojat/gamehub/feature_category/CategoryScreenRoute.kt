@file:Suppress("MatchingDeclarationName")

package ca.hojat.gamehub.feature_category

import ca.hojat.gamehub.common_ui.base.events.Route

sealed class CategoryScreenRoute : Route {
    data class Info(val gameId: Int) : CategoryScreenRoute()
    object Back : CategoryScreenRoute()
}
