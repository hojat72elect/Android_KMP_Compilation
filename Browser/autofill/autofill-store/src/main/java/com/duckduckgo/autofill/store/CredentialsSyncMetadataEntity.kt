

package com.duckduckgo.autofill.store

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "credentials_sync_meta",
    indices = [Index(value = ["syncId"], unique = true)],
)
data class CredentialsSyncMetadataEntity(
    val syncId: String = UUID.randomUUID().toString(),
    @PrimaryKey val localId: Long,
    var deleted_at: String?, // should follow iso8601 format
    var modified_at: String?, // should follow iso8601 format
)
