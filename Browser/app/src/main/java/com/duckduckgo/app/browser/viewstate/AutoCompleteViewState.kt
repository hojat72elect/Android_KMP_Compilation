

package com.duckduckgo.app.browser.viewstate

import com.duckduckgo.app.autocomplete.api.AutoComplete
import com.duckduckgo.app.browser.newtab.FavoritesQuickAccessAdapter

data class AutoCompleteViewState(
    val showSuggestions: Boolean = false,
    val showFavorites: Boolean = false,
    val searchResults: AutoComplete.AutoCompleteResult = AutoComplete.AutoCompleteResult("", emptyList()),
    val favorites: List<FavoritesQuickAccessAdapter.QuickAccessFavorite> = emptyList(),
)
