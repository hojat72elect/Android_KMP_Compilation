package com.aa.android.pokedex.api.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Generic sprite container.
 *
 * @param defaultFront The default sprite for the Pokémon, front-facing.
 */
@JsonClass(generateAdapter = true)
data class SpriteDTO(
    @Json(name = "front_default")
    val defaultFront: String
)
