package com.duckduckgo.adclick.impl.store

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "link_formats")
data class AdClickAttributionLinkFormatEntity(
    @PrimaryKey val url: String,
    val adDomainParameterName: String,
)

@Entity(tableName = "allowlist")
data class AdClickAttributionAllowlistEntity(
    @PrimaryKey val blocklistEntry: String,
    val host: String,
)

@Entity(tableName = "expirations")
data class AdClickAttributionExpirationEntity(
    @PrimaryKey val id: Int = 1,
    val navigationExpiration: Long,
    val totalExpiration: Long,
)

@Entity(tableName = "detections")
data class AdClickAttributionDetectionEntity(
    @PrimaryKey val id: Int = 1,
    val heuristicDetection: String,
    val domainDetection: String,
)
