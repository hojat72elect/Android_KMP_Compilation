

package com.duckduckgo.app.browser.viewstate

import com.duckduckgo.savedsites.api.models.BookmarkFolder
import com.duckduckgo.savedsites.api.models.SavedSite

data class SavedSiteChangedViewState(
    val savedSite: SavedSite,
    val bookmarkFolder: BookmarkFolder?,
)
