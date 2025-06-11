package com.aa.android.pokedex.api.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Contains a type assigned to a Pokémon.
 *
 * @param slot The assigned slot of the type, used for sorting.
 * @param type The type. Name can be retrieved from here.
 */
@JsonClass(generateAdapter = true)
data class PokemonTypeDTO(
    @Json(name = "slot")
    val slot: Int,
    @Json(name = "type")
    val type: ResourceDTO
)
