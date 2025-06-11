

package com.duckduckgo.autofill.impl.email.service

import com.duckduckgo.anvil.annotations.ContributesNonCachingServiceApi
import com.duckduckgo.di.scopes.AppScope
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Query

@ContributesNonCachingServiceApi(AppScope::class)
interface DuckAddressStatusManagementService {

    @GET("$BASE_URL/api/email/addresses")
    suspend fun getActivationStatus(
        @Header("Authorization") authorization: String,
        @Query("address") duckAddress: String,
    ): DuckAddressGetStatusResponse

    @PUT("$BASE_URL/api/email/addresses")
    suspend fun setActivationStatus(
        @Header("Authorization") authorization: String,
        @Query("address") duckAddress: String,
        @Query("active") isActive: Boolean,
    ): DuckAddressGetStatusResponse

    data class DuckAddressGetStatusResponse(
        val active: Boolean,
    )

    companion object {
        private const val BASE_URL = "https://quack.duckduckgo.com"
    }
}

data class EmailAlias(val address: String)
