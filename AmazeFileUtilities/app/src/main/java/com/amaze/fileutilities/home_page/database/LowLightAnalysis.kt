package com.amaze.fileutilities.home_page.database

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

@Keep
@Entity(indices = [Index(value = ["file_path"], unique = true)])
data class LowLightAnalysis(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val uid: Int,
    @ColumnInfo(name = "file_path") val filePath: String,
    @ColumnInfo(name = "is_low_light") val isLowLight: Boolean
) {
    @Ignore
    constructor(
        filePath: String,
        isLowLight: Boolean
    ) :
            this(0, filePath, isLowLight)
}
