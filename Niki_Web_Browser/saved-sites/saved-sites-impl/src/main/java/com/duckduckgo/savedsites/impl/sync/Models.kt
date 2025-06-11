

package com.duckduckgo.savedsites.impl.sync

class SyncBookmarksRequest(
    val bookmarks: SyncBookmarkUpdates,
    val client_timestamp: String,
)

class SyncBookmarkUpdates(
    val updates: List<SyncSavedSitesRequestEntry>,
    val modified_since: String = "0",
)

data class SyncSavedSitesRequestEntry(
    val id: String,
    val title: String?,
    val page: SyncBookmarkPage?,
    val folder: SyncSavedSiteRequestFolder?,
    val deleted: String?,
    val client_last_modified: String?,
)

data class SyncSavedSiteRequestFolder(val children: SyncFolderChildren)

fun SyncSavedSitesRequestEntry.isFolder(): Boolean = this.folder != null
fun SyncSavedSitesRequestEntry.titleOrFallback(): String = this.title ?: "Bookmark"
fun SyncSavedSitesRequestEntry.isBookmark(): Boolean = this.page != null

data class SyncFolderChildren(
    val current: List<String>,
    val insert: List<String>,
    val remove: List<String>,
)

data class SyncSavedSitesResponseEntry(
    val id: String,
    val title: String?,
    val page: SyncBookmarkPage?,
    val folder: SyncSavedSiteResponseFolder?,
    val deleted: String?,
    val last_modified: String?,
)

fun SyncSavedSitesResponseEntry.isFolder(): Boolean = this.folder != null
fun SyncSavedSitesResponseEntry.titleOrFallback(): String = this.title ?: "Bookmark"
fun SyncSavedSitesResponseEntry.isBookmark(): Boolean = this.page != null

data class SyncBookmarkPage(val url: String)
data class SyncSavedSiteResponseFolder(val children: List<String>)
