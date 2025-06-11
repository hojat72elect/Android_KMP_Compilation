

package com.duckduckgo.malicioussiteprotection.impl.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "revisions", primaryKeys = ["feed", "type"])
data class RevisionEntity(
    val feed: String,
    val type: String,
    val revision: Int,
)

@Entity(
    tableName = "hash_prefixes",
)
data class HashPrefixEntity(
    @PrimaryKey
    val hashPrefix: String,
    val type: String,
)

@Entity(
    tableName = "filters",
)
data class FilterEntity(
    @PrimaryKey
    val hash: String,
    val regex: String,
    val type: String,
)
