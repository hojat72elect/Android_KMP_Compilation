

package com.duckduckgo.securestorage.store.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "website_login_credentials")
data class WebsiteLoginCredentialsEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val domain: String?,
    val username: String?,
    val password: String?,
    val passwordIv: String?,
    val notes: String?,
    val notesIv: String?,
    val domainTitle: String?,
    val lastUpdatedInMillis: Long?,
    val lastUsedInMillis: Long?,
)
