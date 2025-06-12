package com.amaze.fileutilities.home_page.database

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Metadata used to process similar images
 */
@Keep
@Entity(indices = [Index(value = ["histogram_checksum"], unique = true)])
data class SimilarImagesAnalysis(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val uid: Int,
    @ColumnInfo(name = "histogram_checksum") val histogram_checksum: String,
    @ColumnInfo(name = "files_path") var files: Set<String>
) {
    @Ignore
    constructor(
        histogramChecksum: String,
        filesPath: Set<String>
    ) :
            this(0, histogramChecksum, filesPath)
}
