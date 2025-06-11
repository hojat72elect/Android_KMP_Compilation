

package com.duckduckgo.voice.impl.remoteconfig

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VoiceSearchSetting(
    @field:Json(name = "excludedManufacturers")
    val excludedManufacturers: List<Manufacturer>,
    @field:Json(name = "excludedLocales")
    val excludedLocales: List<Locale> = emptyList(),
    @field:Json(name = "minVersion")
    val minVersion: Int,
)

@JsonClass(generateAdapter = true)
data class Manufacturer(
    @field:Json(name = "name")
    val name: String,
)

@JsonClass(generateAdapter = true)
data class Locale(
    @field:Json(name = "name")
    val name: String,
)
