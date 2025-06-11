package com.aa.android.pokedex.api.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * A generic list resource returned by the API. Calls made to endpoints without a resource ID or
 * name will return this data structure.
 *
 * @param count The total number of resources at this endpoint.
 * @param next The URL for the next page of data.
 * @param previous The URL for the previous page of data.
 * @param results The list of [ResourceDTO] items.
 */
@JsonClass(generateAdapter = true)
data class ResourceListDTO(
    @Json(name = "count")
    val count: Int,
    @Json(name = "next")
    val next: String,
    @Json(name = "previous")
    val previous: String?,
    @Json(name = "results")
    val results: List<ResourceDTO>
)
