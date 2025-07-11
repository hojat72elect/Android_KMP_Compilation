

package com.duckduckgo.savedsites.impl.sync

import com.duckduckgo.savedsites.api.models.*
import com.duckduckgo.savedsites.store.*
import java.time.*

fun Entity.mapToBookmark(relationId: String): SavedSite.Bookmark =
    SavedSite.Bookmark(this.entityId, this.title, this.url.orEmpty(), relationId, this.lastModified, deleted = this.deletedFlag())

fun Entity.mapToSavedSite(): SavedSite =
    SavedSite.Bookmark(
        id = this.entityId,
        title = this.title,
        url = this.url.orEmpty(),
        lastModified = this.lastModified,
        deleted = this.deletedFlag(),
    )

fun Entity.mapToFavorite(index: Int = 0): SavedSite.Favorite =
    SavedSite.Favorite(
        id = this.entityId,
        title = this.title,
        url = this.url.orEmpty(),
        lastModified = this.lastModified,
        position = index,
        deleted = this.deletedFlag(),
    )

fun Entity.modifiedSince(since: String): Boolean {
    return if (this.lastModified == null) {
        false
    } else {
        val entityModified = OffsetDateTime.parse(this.lastModified)
        val sinceModified = OffsetDateTime.parse(since)
        entityModified.isAfter(sinceModified)
    }
}

fun Entity.deletedFlag(): String? {
    return if (this.deleted) {
        this.lastModified
    } else {
        null
    }
}

fun List<Entity>.mapToFavorites(): List<SavedSite.Favorite> = this.mapIndexed { index, relation -> relation.mapToFavorite(index) }

fun SavedSite.titleOrFallback(): String = this.title.takeIf { it.isNotEmpty() } ?: this.url
