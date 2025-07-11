

package com.duckduckgo.app.trackerdetection.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tds_domain_entity")
data class TdsDomainEntity(
    @PrimaryKey val domain: String,
    val entityName: String,
)
