package com.amaze.fileutilities.home_page.database

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["package_name"], unique = true)])
@Keep
data class InstalledApps(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val uid: Int,
    @ColumnInfo(name = "package_name") val packageName: String,
    @ColumnInfo(name = "data_dirs") val dataDirs: List<String>,
) {
    @Ignore
    constructor(
        packageName: String,
        dataDirs: List<String>,
    ) :
            this(0, packageName, dataDirs)
}
