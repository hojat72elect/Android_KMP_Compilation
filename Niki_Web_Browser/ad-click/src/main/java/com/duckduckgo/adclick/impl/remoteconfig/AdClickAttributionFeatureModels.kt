package com.duckduckgo.adclick.impl.remoteconfig

import com.squareup.moshi.Json

data class AdClickAttributionFeatureModel(
    @field:Json(name = "linkFormats")
    val linkFormats: List<AdClickAttributionLinkFormat>,
    @field:Json(name = "allowlist")
    val allowlist: List<AdClickAttributionAllowlist>,
    @field:Json(name = "navigationExpiration")
    val navigationExpiration: Long,
    @field:Json(name = "totalExpiration")
    val totalExpiration: Long,
    @field:Json(name = "heuristicDetection")
    val heuristicDetection: String?,
    @field:Json(name = "domainDetection")
    val domainDetection: String?,
)

data class AdClickAttributionLinkFormat(
    @field:Json(name = "url")
    val url: String,
    @field:Json(name = "adDomainParameterName")
    val adDomainParameterName: String?,
)

data class AdClickAttributionAllowlist(
    @field:Json(name = "blocklistEntry")
    val blocklistEntry: String?,
    @field:Json(name = "host")
    val host: String?,
)
