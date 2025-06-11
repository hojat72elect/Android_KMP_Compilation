

package com.duckduckgo.savedsites.impl.sync.algorithm

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.savedsites.api.SavedSitesRepository
import com.duckduckgo.savedsites.api.models.BookmarkFolder
import com.duckduckgo.savedsites.api.models.SavedSite.Bookmark
import com.duckduckgo.savedsites.impl.sync.algorithm.SavedSitesDuplicateResult.Duplicate
import com.duckduckgo.savedsites.impl.sync.algorithm.SavedSitesDuplicateResult.NotDuplicate
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import timber.log.*

interface SavedSitesDuplicateFinder {

    fun findFolderDuplicate(
        bookmarkFolder: BookmarkFolder,
    ): SavedSitesDuplicateResult

    fun findBookmarkDuplicate(bookmark: Bookmark): SavedSitesDuplicateResult
}

sealed class SavedSitesDuplicateResult {
    object NotDuplicate : SavedSitesDuplicateResult()
    data class Duplicate(val id: String) : SavedSitesDuplicateResult()
}

@ContributesBinding(AppScope::class)
class RealSavedSitesDuplicateFinder @Inject constructor(
    val repository: SavedSitesRepository,
) : SavedSitesDuplicateFinder {
    override fun findFolderDuplicate(bookmarkFolder: BookmarkFolder): SavedSitesDuplicateResult {
        val existingFolder = repository.getFolderByName(bookmarkFolder.name)
        return if (existingFolder != null) {
            if (existingFolder.parentId == bookmarkFolder.parentId) {
                Duplicate(existingFolder.id)
            } else {
                NotDuplicate
            }
        } else {
            NotDuplicate
        }
    }

    override fun findBookmarkDuplicate(bookmark: Bookmark): SavedSitesDuplicateResult {
        val presentUrl = repository.getBookmark(bookmark.url)
        return if (presentUrl != null) {
            if (presentUrl.title == bookmark.title && presentUrl.parentId == bookmark.parentId) {
                Duplicate(presentUrl.id)
            } else {
                NotDuplicate
            }
        } else {
            NotDuplicate
        }
    }
}
