

package com.duckduckgo.savedsites.impl.sync.algorithm

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.savedsites.api.SavedSitesRepository
import com.duckduckgo.savedsites.api.models.BookmarkFolder
import com.duckduckgo.savedsites.api.models.SavedSite.Bookmark
import com.duckduckgo.savedsites.impl.sync.SyncSavedSitesRepository
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Named
import timber.log.Timber

@ContributesBinding(AppScope::class)
@Named("localWinsStrategy")
class SavedSitesLocalWinsPersister @Inject constructor(
    private val savedSitesRepository: SavedSitesRepository,
    private val syncSavedSitesRepository: SyncSavedSitesRepository,
) : SavedSitesSyncPersisterStrategy {
    override fun processBookmarkFolder(
        folder: BookmarkFolder,
        children: List<String>,
    ) {
        val localFolder = savedSitesRepository.getFolder(folder.id)
        if (localFolder != null) {
            if (folder.isDeleted()) {
                Timber.d("Sync-Bookmarks-Persister: folder ${localFolder.id} exists locally but was deleted remotely, deleting locally too")
                savedSitesRepository.delete(localFolder)
            } else {
                Timber.d("Sync-Bookmarks-Persister: folder ${localFolder.id} exists locally, nothing to do")
            }
        } else {
            if (folder.isDeleted()) {
                Timber.d("Sync-Bookmarks-Persister: folder ${folder.id} not present locally but was deleted, nothing to do")
            } else {
                Timber.d("Sync-Bookmarks-Persister: folder ${folder.id} not present locally, inserting")
                savedSitesRepository.insert(folder)
            }
        }
    }

    override fun processFavouritesFolder(
        favouriteFolder: String,
        children: List<String>,
    ) {
        // process favourites means replacing the current children with the remote
        syncSavedSitesRepository.replaceFavouriteFolder(favouriteFolder, children)
    }

    override fun processBookmark(
        bookmark: Bookmark,
        folderId: String,
    ) {
        val storedBookmark = savedSitesRepository.getBookmarkById(bookmark.id)
        if (storedBookmark != null) {
            if (bookmark.isDeleted()) {
                Timber.d("Sync-Bookmarks-Persister: remote bookmark ${bookmark.id} exists locally but was deleted remotely, deleting locally too")
                savedSitesRepository.delete(bookmark)
            } else {
                Timber.d("Sync-Bookmarks-Persister: remote bookmark ${bookmark.id} exists locally, nothing to do")
            }
        } else {
            if (bookmark.isDeleted()) {
                Timber.d("Sync-Bookmarks-Persister: bookmark ${bookmark.id} not present locally but was deleted, nothing to do")
            } else {
                Timber.d("Sync-Bookmarks-Persister: child ${bookmark.id} not present locally, inserting")
                savedSitesRepository.insert(bookmark)
            }
        }
    }
}
