

package com.duckduckgo.malicioussiteprotection.impl.data.network

import com.duckduckgo.common.utils.AppUrl.Url.API
import com.squareup.moshi.Json
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "$API/api/protection/v2/android"

interface MaliciousSiteService {

    @AuthRequired
    @GET("$BASE_URL/matches")
    suspend fun getMatches(@Query("hashPrefix") hashPrefix: String): MatchesResponse

    @AuthRequired
    @GET("$BASE_URL/revision")
    suspend fun getRevision(): RevisionResponse
}

data class MatchesResponse(
    val matches: List<MatchResponse>,
)

data class MatchResponse(
    val hostname: String,
    val url: String,
    val regex: String,
    val hash: String,
    @field:Json(name = "category")
    val feed: String,
)

data class RevisionResponse(
    val revision: Int,
)
