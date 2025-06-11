package ca.hojat.gamehub.core.data.api.igdb.games.entities

import ca.hojat.gamehub.core.data.api.igdbcalypse.serialization.annotations.Apicalypse
import ca.hojat.gamehub.core.data.api.igdbcalypse.serialization.annotations.ApicalypseClass
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@ApicalypseClass
@Serializable
data class ApiWebsite(
    @Apicalypse(Schema.ID)
    @SerialName(Schema.ID)
    val id: Int,
    @Apicalypse(Schema.URL)
    @SerialName(Schema.URL)
    val url: String,
    @Apicalypse(Schema.CATEGORY)
    @SerialName(Schema.CATEGORY)
    val category: ApiWebsiteCategory,
) {

    object Schema {
        const val ID = "id"
        const val URL = "url"
        const val CATEGORY = "category"
    }
}
