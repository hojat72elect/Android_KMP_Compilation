

package com.duckduckgo.app.statistics.api

import com.duckduckgo.anvil.annotations.ContributesServiceApi
import com.duckduckgo.app.statistics.BuildConfig
import com.duckduckgo.app.statistics.model.Atb
import com.duckduckgo.common.utils.AppUrl.ParamKey
import com.duckduckgo.di.scopes.AppScope
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

@ContributesServiceApi(AppScope::class)
interface StatisticsService {

    @GET("/exti/")
    fun exti(
        @Query(ParamKey.ATB) atb: String,
        @Query(ParamKey.DEV_MODE) devMode: Int? = if (BuildConfig.DEBUG) 1 else null,
    ): Observable<ResponseBody>

    @GET("/atb.js")
    fun atb(
        @Query(ParamKey.DEV_MODE) devMode: Int? = if (BuildConfig.DEBUG) 1 else null,
        @Query(ParamKey.EMAIL) email: Int?,
    ): Observable<Atb>

    // ANA
    @GET("/atb.js")
    fun updateSearchAtb(
        @Query(ParamKey.ATB) atb: String,
        @Query(ParamKey.RETENTION_ATB) retentionAtb: String,
        @Query(ParamKey.DEV_MODE) devMode: Int? = if (BuildConfig.DEBUG) 1 else null,
        @Query(ParamKey.EMAIL) email: Int?,
    ): Observable<Atb>

    // ANA
    @GET("/atb.js?at=app_use")
    fun updateAppAtb(
        @Query(ParamKey.ATB) atb: String,
        @Query(ParamKey.RETENTION_ATB) retentionAtb: String,
        @Query(ParamKey.DEV_MODE) devMode: Int? = if (BuildConfig.DEBUG) 1 else null,
        @Query(ParamKey.EMAIL) email: Int?,
    ): Observable<Atb>
}
