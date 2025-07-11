

package com.duckduckgo.app.bookmarks.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmark_folders")
data class BookmarkFolderEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var name: String,
    var parentId: Long,
)
