package com.amaze.fileutilities.home_page.database

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

@Keep
@Entity(indices = [Index(value = ["file_path"], unique = true)])
data class VideoPlayerState(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val uid: Int,
    @ColumnInfo(name = "file_path") val filePath: String,
    @ColumnInfo(name = "playback_position") val playbackPosition: Long,
) {
    @Ignore
    constructor(
        filePath: String,
        playbackPosition: Long
    ) : this(0, filePath, playbackPosition)
}
