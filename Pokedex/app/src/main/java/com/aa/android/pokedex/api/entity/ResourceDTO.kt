package com.aa.android.pokedex.api.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Generic resource to represent an item in the API.
 *
 * @param name The name of the resource.
 * @param url The API url for the resource.
 */
@JsonClass(generateAdapter = true)
data class ResourceDTO(
    @Json(name = "name")
    val name: String,
    @Json(name = "url")
    val url: String
)
