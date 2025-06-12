

package com.amaze.fileutilities.home_page.database

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * While fetching and processing, be sure to validate that file exists
 */
@Keep
@Entity(indices = [Index(value = ["sha256_checksum"], unique = true)])
data class InternalStorageAnalysis(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val uid: Int,
    @ColumnInfo(name = "sha256_checksum") val checksum: String,
    @ColumnInfo(name = "files_path") var files: List<String>,
    @ColumnInfo(name = "is_empty") val isEmpty: Boolean,
    @ColumnInfo(name = "is_junk") val isJunk: Boolean,
    @ColumnInfo(name = "is_directory") val isDirectory: Boolean,
    @ColumnInfo(name = "is_mediastore") val isMediaStore: Boolean,
    @ColumnInfo(name = "depth") val depth: Int
) {
    constructor(
        checksum: String,
        filesPath: List<String>,
        isEmpty: Boolean,
        isJunk: Boolean,
        isDirectory: Boolean,
        isMediaStore: Boolean,
        depth: Int
    ) :
        this(0, checksum, filesPath, isEmpty, isJunk, isDirectory, isMediaStore, depth)
}
