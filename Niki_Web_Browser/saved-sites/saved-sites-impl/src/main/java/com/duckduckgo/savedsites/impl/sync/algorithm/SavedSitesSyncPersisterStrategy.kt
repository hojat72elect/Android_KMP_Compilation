

package com.duckduckgo.savedsites.impl.sync.algorithm

import com.duckduckgo.savedsites.api.models.BookmarkFolder
import com.duckduckgo.savedsites.api.models.SavedSite.Bookmark

interface SavedSitesSyncPersisterStrategy {

    fun processBookmarkFolder(
        folder: BookmarkFolder,
        children: List<String>,
    )

    /**
     * Favourites folder have the same id and names across platforms
     * All we want to do here is deal with the children and mark them as favourites or not
     * Other methods deal with inserting the actual Entity
     */
    fun processFavouritesFolder(
        favouriteFolder: String,
        children: List<String>,
    )

    fun processBookmark(
        bookmark: Bookmark,
        folderId: String,
    )
}
