package ca.hojat.gamehub.core.data.api.igdb.games.entities

import ca.hojat.gamehub.core.data.api.igdbcalypse.serialization.annotations.Apicalypse
import ca.hojat.gamehub.core.data.api.igdbcalypse.serialization.annotations.ApicalypseClass
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The perspective that the player will have while playing the game.
 * Such as 1st person, 3rd person, strategy, and so on...
 */
@ApicalypseClass
@Serializable
data class ApiPlayerPerspective(
    @Apicalypse(Schema.NAME)
    @SerialName(Schema.NAME)
    val name: String,
) {

    object Schema {
        const val NAME = "name"
    }
}
