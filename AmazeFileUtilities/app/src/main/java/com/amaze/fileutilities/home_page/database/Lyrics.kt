package com.amaze.fileutilities.home_page.database

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["file_path"], unique = true)])
@Keep
data class Lyrics(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val uid: Int,
    @ColumnInfo(name = "file_path") val filePath: String,
    @ColumnInfo(name = "lyrics_text") val lyricsText: String,
    @ColumnInfo(name = "is_synced") val isSynced: Boolean
) {
    @Ignore
    constructor(
        filePath: String,
        lyricsText: String,
        isSynced: Boolean
    ) :
            this(0, filePath, lyricsText, isSynced)
}
