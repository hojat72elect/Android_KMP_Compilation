package ca.hojat.gamehub.core.data.api.igdb.games.entities

import ca.hojat.gamehub.core.data.api.igdbcalypse.serialization.annotations.Apicalypse
import ca.hojat.gamehub.core.data.api.igdbcalypse.serialization.annotations.ApicalypseClass
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Such as single player, multiplayer, and so on.
 */
@ApicalypseClass
@Serializable
data class ApiMode(
    @Apicalypse(Schema.NAME)
    @SerialName(Schema.NAME)
    val name: String,
) {

    object Schema {
        const val NAME = "name"
    }
}
