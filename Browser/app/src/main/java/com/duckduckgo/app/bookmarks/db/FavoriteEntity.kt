package com.duckduckgo.app.bookmarks.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorites",
    indices = [Index(value = ["title", "url"], unique = true)],
)
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var title: String,
    var url: String,
    var position: Int,
)
