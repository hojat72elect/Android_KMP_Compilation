package com.duckduckgo.app.anrs.store

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "uncaught_exception_entity")
data class ExceptionEntity(
    @PrimaryKey val hash: String,
    val shortName: String,
    val processName: String,
    val message: String,
    val stackTrace: String,
    val version: String,
    val timestamp: String,
    val webView: String,
    val customTab: Boolean,
)
