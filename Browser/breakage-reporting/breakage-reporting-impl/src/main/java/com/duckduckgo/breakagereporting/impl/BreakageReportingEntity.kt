package com.duckduckgo.breakagereporting.impl

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "breakage_reporting")
data class BreakageReportingEntity(
    @PrimaryKey val id: Int = 1,
    val json: String,
)
