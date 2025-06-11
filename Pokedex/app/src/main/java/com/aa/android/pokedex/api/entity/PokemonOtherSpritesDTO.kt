package com.aa.android.pokedex.api.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Contains other sprite URLs for a Pokémon.
 *
 * @param dreamWorldSprite The Dream World sprite url. Typically SVG.
 * @param homeSprite Pokemon sprite in 3D style. Typically PNG.
 * @param officialArtworkSprite Official Pokémon artwork. Typically PNG.
 */
@JsonClass(generateAdapter = true)
data class PokemonOtherSpritesDTO(
    @Json(name = "dream_world")
    val dreamWorldSprite: SpriteDTO,
    @Json(name = "home")
    val homeSprite: SpriteDTO,
    @Json(name = "official-artwork")
    val officialArtworkSprite: SpriteDTO
)
