package ca.hojat.gamehub.core.data.database.games.entities

import kotlinx.serialization.Serializable

@Serializable
data class DbWebsite(
    val id: Int,
    val url: String,
    val category: DbWebsiteCategory
)
