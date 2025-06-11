

package com.duckduckgo.savedsites.impl.sync.algorithm

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.savedsites.api.SavedSitesRepository
import com.duckduckgo.savedsites.api.models.BookmarkFolder
import com.duckduckgo.savedsites.api.models.SavedSite.Bookmark
import com.duckduckgo.savedsites.impl.sync.SyncSavedSitesRepository
import com.duckduckgo.savedsites.impl.sync.algorithm.SavedSitesDuplicateResult.Duplicate
import com.duckduckgo.savedsites.impl.sync.algorithm.SavedSitesDuplicateResult.NotDuplicate
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Named
import timber.log.Timber

@ContributesBinding(AppScope::class)
@Named("deduplicationStrategy")
class SavedSitesDeduplicationPersister @Inject constructor(
    private val savedSitesRepository: SavedSitesRepository,
    private val syncSavedSitesRepository: SyncSavedSitesRepository,
    private val duplicateFinder: SavedSitesDuplicateFinder,
) : SavedSitesSyncPersisterStrategy {
    override fun processBookmarkFolder(
        folder: BookmarkFolder,
        children: List<String>,
    ) {
        // in deduplication we replace local folder with remote folder (id, name, parentId, add children to existent ones)
        // we don't replace the children here, because they might be children already present before the deduplication
        when (val result = duplicateFinder.findFolderDuplicate(folder)) {
            is Duplicate -> {
                if (folder.isDeleted()) {
                    Timber.d("Sync-Bookmarks-Persister: folder ${folder.id} has a local duplicate in ${result.id} and needs to be deleted")
                    savedSitesRepository.delete(folder)
                } else {
                    Timber.d("Sync-Bookmarks-Persister: folder ${folder.id} has a local duplicate in ${result.id}, replacing content")
                    savedSitesRepository.replaceFolderContent(folder, result.id)
                }
            }

            is NotDuplicate -> {
                if (folder.isDeleted()) {
                    Timber.d("Sync-Bookmarks-Persister: folder ${folder.id} not present locally but was deleted, nothing to do")
                } else {
                    Timber.d("Sync-Bookmarks-Persister: folder ${folder.id} not present locally, inserting")
                    savedSitesRepository.insert(folder)
                }
            }
        }
    }

    override fun processFavouritesFolder(
        favouriteFolder: String,
        children: List<String>,
    ) {
        // deduplicating favourites means adding remote favourites to local folder
        // and ensuring the folder will be sent in the next sync operation if there were local favourites
        syncSavedSitesRepository.addToFavouriteFolder(favouriteFolder, children)
    }

    override fun processBookmark(
        bookmark: Bookmark,
        folderId: String,
    ) {
        if (bookmark.isDeleted()) {
            Timber.d("Sync-Bookmarks-Persister: child ${bookmark.id} is removed and not present locally, nothing to do")
        } else {
            // if there's a bookmark duplicate locally (url and name) then we replace it
            when (val result = duplicateFinder.findBookmarkDuplicate(bookmark)) {
                is Duplicate -> {
                    Timber.d("Sync-Bookmarks-Persister: child ${bookmark.id} has a local duplicate in ${result.id}, replacing")
                    syncSavedSitesRepository.replaceBookmark(bookmark, result.id)
                }

                is NotDuplicate -> {
                    Timber.d("Sync-Bookmarks-Persister: child ${bookmark.id} not present locally, inserting")
                    savedSitesRepository.insert(bookmark)
                }
            }
        }
    }
}
