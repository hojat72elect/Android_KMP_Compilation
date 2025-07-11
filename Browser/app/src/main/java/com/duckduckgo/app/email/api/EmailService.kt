

package com.duckduckgo.app.email.api

import com.duckduckgo.anvil.annotations.ContributesNonCachingServiceApi
import com.duckduckgo.di.scopes.AppScope
import retrofit2.http.Header
import retrofit2.http.POST

@ContributesNonCachingServiceApi(AppScope::class)
interface EmailService {
    @POST("https://quack.duckduckgo.com/api/email/addresses")
    suspend fun newAlias(@Header("Authorization") authorization: String): EmailAlias
}

data class EmailAlias(val address: String)
