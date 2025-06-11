

package com.duckduckgo.app.autocomplete.api

import com.duckduckgo.anvil.annotations.ContributesNonCachingServiceApi
import com.duckduckgo.common.utils.AppUrl
import com.duckduckgo.di.scopes.AppScope
import java.util.*
import retrofit2.http.GET
import retrofit2.http.Query

@ContributesNonCachingServiceApi(AppScope::class)
interface AutoCompleteService {

    @GET("${AppUrl.Url.API}/ac/")
    suspend fun autoComplete(
        @Query("q") query: String,
        @Query("kl") languageCode: String = Locale.getDefault().language,
        @Query("is_nav") nav: String = "1",
    ): List<AutoCompleteServiceRawResult>
}

data class AutoCompleteServiceRawResult(
    val phrase: String,
    val isNav: Boolean?,
)
