

package com.duckduckgo.savedsites.impl.bookmarks

import com.duckduckgo.savedsites.impl.bookmarks.BookmarksAdapter.BookmarkFolderItem
import com.duckduckgo.savedsites.impl.bookmarks.BookmarksAdapter.BookmarkItem
import com.duckduckgo.savedsites.impl.bookmarks.BookmarksAdapter.BookmarksItemTypes
import com.duckduckgo.savedsites.impl.bookmarks.BookmarksAdapter.EmptyHint
import com.duckduckgo.savedsites.impl.bookmarks.BookmarksAdapter.EmptySearchHint
import java.text.Collator

class BookmarksNameSortingComparator : Comparator<BookmarksItemTypes> {
    override fun compare(
        p0: BookmarksItemTypes?,
        p1: BookmarksItemTypes?,
    ): Int {
        if (p0 == null && p1 == null) return 0
        if (p0 == null) return -1
        if (p1 == null) return 1

        with(buildCollator()) {
            val titles = extractTitles(p0, p1)
            return compareFields(titles.first, titles.second)
        }
    }

    private fun extractTitles(
        p0: BookmarksItemTypes,
        p1: BookmarksItemTypes,
    ): Pair<String?, String?> {
        return Pair(extractTitle(p0), extractTitle(p1))
    }

    private fun extractTitle(bookmarkItemType: BookmarksItemTypes): String? {
        return when (bookmarkItemType) {
            is BookmarkItem -> bookmarkItemType.bookmark.title.lowercase()
            is BookmarkFolderItem -> bookmarkItemType.bookmarkFolder.name.lowercase()
            EmptyHint, EmptySearchHint -> null
        }
    }

    private fun Collator.compareFields(
        field1: String?,
        field2: String?,
    ): Int {
        if (field1 == null && field2 == null) return 0
        if (field1 == null) return -1
        if (field2 == null) return 1
        return getCollationKey(field1).compareTo(getCollationKey(field2))
    }

    private fun buildCollator(): Collator {
        val coll: Collator = Collator.getInstance()
        coll.strength = Collator.SECONDARY
        return coll
    }
}
