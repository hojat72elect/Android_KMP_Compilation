

package com.duckduckgo.app.trackerdetection.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tds_entity")
data class TdsEntity(
    @PrimaryKey override val name: String,
    override val displayName: String,
    override val prevalence: Double,
) : com.duckduckgo.app.trackerdetection.model.Entity
