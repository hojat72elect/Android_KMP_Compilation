package com.amaze.fileutilities.home_page.database

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * While processing similar images
 */
@Keep
@Entity(indices = [Index(value = ["file_path", "parent_path"], unique = true)])
data class SimilarImagesAnalysisMetadata(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val uid: Int,
    @ColumnInfo(name = "parent_path") val parentPath: String,
    @ColumnInfo(name = "file_path") val filePath: String,
    @ColumnInfo(name = "blue_channel") val blueChannel: List<Pair<Int, Int>>,
    @ColumnInfo(name = "green_channel") val greenChannel: List<Pair<Int, Int>>,
    @ColumnInfo(name = "red_channel") val redChannel: List<Pair<Int, Int>>,
    @ColumnInfo(name = "datapoints") val datapoints: Int,
    @ColumnInfo(name = "threshold") val threshold: Int,
    @ColumnInfo(name = "is_analysed") var isAnalysed: Boolean
) {
    @Ignore
    constructor(
        parentPath: String,
        filePath: String,
        blueChannel: List<Pair<Int, Int>>,
        greenChannel: List<Pair<Int, Int>>,
        redChannel: List<Pair<Int, Int>>,
        datapoints: Int,
        threshold: Int
    ) :
            this(
                0, parentPath, filePath, blueChannel, greenChannel, redChannel, datapoints,
                threshold, false
            )
}
