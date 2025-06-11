package ca.hojat.gamehub.core.data.api.igdb.games.entities

import ca.hojat.gamehub.core.data.api.igdbcalypse.serialization.annotations.Apicalypse
import ca.hojat.gamehub.core.data.api.igdbcalypse.serialization.annotations.ApicalypseClass
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@ApicalypseClass
@Serializable
data class ApiImage(
    @Apicalypse(Schema.ID)
    @SerialName(Schema.ID)
    val id: String,
    @Apicalypse(Schema.WIDTH)
    @SerialName(Schema.WIDTH)
    val width: Int? = null,
    @Apicalypse(Schema.HEIGHT)
    @SerialName(Schema.HEIGHT)
    val height: Int? = null,
) {

    object Schema {
        const val ID = "image_id"
        const val WIDTH = "width"
        const val HEIGHT = "height"
    }
}
