

package com.duckduckgo.app.surrogates.api

import com.duckduckgo.anvil.annotations.ContributesServiceApi
import com.duckduckgo.di.scopes.AppScope
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

@ContributesServiceApi(AppScope::class)
interface ResourceSurrogateListService {

    @GET("https://staticcdn.duckduckgo.com/surrogates.txt")
    fun surrogates(): Call<ResponseBody>
}
