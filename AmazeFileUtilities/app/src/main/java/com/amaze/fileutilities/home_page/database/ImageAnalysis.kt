package com.amaze.fileutilities.home_page.database

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * While fetching and processing, be sure to validate that file exists
 */
@Keep
@Entity(indices = [Index(value = ["file_path"], unique = true)])
data class ImageAnalysis(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val uid: Int,
    @ColumnInfo(name = "file_path") val filePath: String,
    @ColumnInfo(name = "is_sad") val isSad: Boolean,
    @ColumnInfo(name = "is_distracted") val isDistracted: Boolean,
    @ColumnInfo(name = "is_sleeping") val isSleeping: Boolean,
    @ColumnInfo(name = "face_count") val faceCount: Int,
) {
    @Ignore
    constructor(
        filePath: String,
        isSad: Boolean,
        isDistracted: Boolean,
        isSleeping: Boolean,
        faceCount: Int
    ) :
            this(0, filePath, isSad, isDistracted, isSleeping, faceCount)
}
