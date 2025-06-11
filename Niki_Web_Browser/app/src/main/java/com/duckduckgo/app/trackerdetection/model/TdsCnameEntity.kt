

package com.duckduckgo.app.trackerdetection.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tds_cname_entity")
data class TdsCnameEntity(
    @PrimaryKey val cloakedHostName: String,
    val uncloakedHostName: String,
)
