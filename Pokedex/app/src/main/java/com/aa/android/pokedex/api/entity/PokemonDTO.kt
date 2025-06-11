package com.aa.android.pokedex.api.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Data object representing a single Pokémon.
 *
 * @param name The name of the Pokémon.
 * @param height The height of the Pokémon in decimeters.
 * @param weight The weight of the Pokémon in hectograms.
 * @param types The type of the Pokémon. Can have multiple.
 * @param stats The stat values of the Pokémon - HP, Attack, Defense, etc.
 * @param sprites Various image URLs for the Pokémon.
 */
@JsonClass(generateAdapter = true)
data class PokemonDTO(
    @Json(name = "name")
    val name: String,
    @Json(name = "height")
    val height: Int,
    @Json(name = "weight")
    val weight: Int,
    @Json(name = "types")
    val types: List<PokemonTypeDTO>,
    @Json(name = "stats")
    val stats: List<PokemonStatDTO>,
    @Json(name = "sprites")
    val sprites: PokemonSpritesDTO
)
