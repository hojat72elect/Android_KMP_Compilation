

package com.duckduckgo.voice.store

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "voice_search_manufacturers")
data class ManufacturerEntity(
    @PrimaryKey
    val name: String,
)

@Entity(tableName = "voice_search_locales")
data class LocaleEntity(
    @PrimaryKey
    val name: String,
)

@Entity(tableName = "voice_search_min_version")
data class MinVersionEntity(
    @PrimaryKey
    val minVersion: Int,
)
