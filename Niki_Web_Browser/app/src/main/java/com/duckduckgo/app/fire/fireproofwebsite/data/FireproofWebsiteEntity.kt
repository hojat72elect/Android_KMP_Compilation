

package com.duckduckgo.app.fire.fireproofwebsite.data

import androidx.room.Entity
import androidx.room.PrimaryKey

private const val WWW_PREFIX = "www."

@Entity(tableName = "fireproofWebsites")
data class FireproofWebsiteEntity(
    @PrimaryKey val domain: String,
)

fun FireproofWebsiteEntity.website(): String {
    return domain.takeIf { it.startsWith(WWW_PREFIX, ignoreCase = true) }
        ?.drop(WWW_PREFIX.length) ?: domain
}
