

package com.duckduckgo.app.statistics.api

import com.duckduckgo.anvil.annotations.ContributesNonCachingServiceApi
import com.duckduckgo.common.utils.AppUrl
import com.duckduckgo.common.utils.BuildConfig
import com.duckduckgo.di.scopes.AppScope
import io.reactivex.Completable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

@ContributesNonCachingServiceApi(AppScope::class)
interface PixelService {

    @GET("${AppUrl.Url.PIXEL}/t/{pixelName}_android_{formFactor}")
    fun fire(
        @Path("pixelName") pixelName: String,
        @Path("formFactor") formFactor: String,
        @Query(AppUrl.ParamKey.ATB) atb: String,
        @QueryMap additionalQueryParams: Map<String, String> = emptyMap(),
        @QueryMap(encoded = true) encodedQueryParams: Map<String, String> = emptyMap(),
        @Query(AppUrl.ParamKey.DEV_MODE) devMode: Int? = if (BuildConfig.DEBUG) 1 else null,
    ): Completable
}
