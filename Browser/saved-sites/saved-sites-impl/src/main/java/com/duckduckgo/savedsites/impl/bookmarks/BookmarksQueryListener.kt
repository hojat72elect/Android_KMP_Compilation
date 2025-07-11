

package com.duckduckgo.savedsites.impl.bookmarks

import androidx.lifecycle.viewModelScope
import com.duckduckgo.common.utils.ConflatedJob
import com.duckduckgo.savedsites.api.models.SavedSite.Favorite
import com.duckduckgo.savedsites.impl.bookmarks.BookmarksAdapter.BookmarkFolderItem
import com.duckduckgo.savedsites.impl.bookmarks.BookmarksAdapter.BookmarkItem
import com.duckduckgo.savedsites.impl.bookmarks.BookmarksAdapter.BookmarksItemTypes
import java.util.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BookmarksQueryListener(
    private val viewModel: BookmarksViewModel,
    private val bookmarksAdapter: BookmarksAdapter,
) {

    private var searchJob = ConflatedJob()

    fun onQueryTextChange(newText: String) {
        searchJob += viewModel.viewModelScope.launch {
            delay(DEBOUNCE_PERIOD)
            viewModel.onSearchQueryUpdated(newText)
            val favorites = viewModel.viewState.value?.favorites
            viewModel.viewState.value?.sortedItems?.let { bookmarks ->
                val filteredBookmarks = filterBookmarks(newText, bookmarks, favorites)
                bookmarksAdapter.setItems(filteredBookmarks, false, true)
            }
        }
    }

    fun cancelSearch() {
        searchJob.cancel()
    }

    private fun filterBookmarks(
        query: String,
        bookmarks: List<BookmarksItemTypes>,
        favorites: List<Favorite>?,
    ): List<BookmarksItemTypes> {
        val lowercaseQuery = query.lowercase(Locale.getDefault())
        return bookmarks.filter {
            when (it) {
                is BookmarkItem -> {
                    val lowercaseTitle = it.bookmark.title.lowercase(Locale.getDefault())
                    lowercaseTitle.contains(lowercaseQuery) || it.bookmark.url.contains(lowercaseQuery)
                }
                is BookmarkFolderItem -> {
                    val lowercaseTitle = it.bookmarkFolder.name.lowercase(Locale.getDefault())
                    lowercaseTitle.contains(lowercaseQuery)
                }
                else -> false
            }
        }.map {
            when (it) {
                is BookmarkItem -> {
                    val isFavorite = favorites?.any { favorite -> favorite.id == it.bookmark.id } ?: false
                    BookmarkItem(it.bookmark.copy(isFavorite = isFavorite))
                }
                is BookmarkFolderItem -> it
                else -> throw IllegalStateException("Unknown bookmarks item type")
            }
        }
    }

    companion object {
        private const val DEBOUNCE_PERIOD = 400L
    }
}
