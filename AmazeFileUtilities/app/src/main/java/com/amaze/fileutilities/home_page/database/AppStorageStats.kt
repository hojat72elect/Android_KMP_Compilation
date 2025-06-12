package com.amaze.fileutilities.home_page.database

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    indices = [
        Index(value = ["timestamp", "package_name"], unique = true)
    ]
)
@Keep
data class AppStorageStats(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val uid: Int,
    @ColumnInfo(name = "package_name") val packageName: String,
    @ColumnInfo(name = "timestamp") val timestamp: Date,
    @ColumnInfo(name = "package_size") val packageSize: Long
) {
    @Ignore
    constructor(
        packageName: String,
        timestamp: Date,
        packageSize: Long
    ) : this(0, packageName, timestamp, packageSize)
}
