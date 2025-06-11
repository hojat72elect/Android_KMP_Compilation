

package com.duckduckgo.downloads.impl

import com.duckduckgo.anvil.annotations.ContributesServiceApi
import com.duckduckgo.di.scopes.AppScope
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.HEAD
import retrofit2.http.Header
import retrofit2.http.Streaming
import retrofit2.http.Url

@ContributesServiceApi(AppScope::class)
interface DownloadFileService {

    @HEAD
    fun getFileDetails(@Url urlString: String): Call<Void>?

    @Streaming
    @GET
    fun downloadFile(
        @Header("Cookie") cookie: String,
        @Url urlString: String,
    ): Call<ResponseBody>
}
