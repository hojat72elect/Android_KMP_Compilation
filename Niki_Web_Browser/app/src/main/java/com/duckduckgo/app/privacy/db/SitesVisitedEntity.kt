

package com.duckduckgo.app.privacy.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sites_visited")
data class SitesVisitedEntity(
    @PrimaryKey val key: String = SINGLETON_KEY,
    val count: Int,
) {
    companion object {
        const val SINGLETON_KEY = "SINGLETON_KEY"
    }
}
