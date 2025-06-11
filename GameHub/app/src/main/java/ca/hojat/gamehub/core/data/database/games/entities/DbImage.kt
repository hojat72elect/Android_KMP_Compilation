package ca.hojat.gamehub.core.data.database.games.entities

import kotlinx.serialization.Serializable

@Serializable
data class DbImage(
    val id: String,
    val width: Int?,
    val height: Int?
)
