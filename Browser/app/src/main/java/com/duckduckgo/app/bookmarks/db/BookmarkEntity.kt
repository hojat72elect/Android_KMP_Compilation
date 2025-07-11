

package com.duckduckgo.app.bookmarks.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * If this is modified, please consider the following instructions carefully:
 *
 * Every time  we change BookmarkEntity we also need to check (and probably change)
 * @property com.duckduckgo.app.global.db.MigrationsProvider.BOOKMARKS_DB_ON_CREATE
 * which is located in AppDatabase.kt
 */
@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var title: String?,
    var url: String,
    var parentId: Long,
)
