package com.aa.android.pokedex.api.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Contains data representing a Pokémon's stats.
 *
 * @param baseStat The value of the stat.
 * @param effort The effort points (EV) the Pokémon has in this stat.
 * @param stat The stat. Name is contained in this object.
 */
@JsonClass(generateAdapter = true)
data class PokemonStatDTO(
    @Json(name = "base_stat")
    val baseStat: Int,
    @Json(name = "effort")
    val effort: Int,
    @Json(name = "stat")
    val stat: ResourceDTO
)
