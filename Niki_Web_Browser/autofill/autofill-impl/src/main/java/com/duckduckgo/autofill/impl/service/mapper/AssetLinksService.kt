

package com.duckduckgo.autofill.impl.service.mapper

import com.duckduckgo.anvil.annotations.ContributesServiceApi
import com.duckduckgo.di.scopes.AppScope
import retrofit2.http.GET
import retrofit2.http.Url

@ContributesServiceApi(AppScope::class)
interface AssetLinksService {
    @GET
    suspend fun getAssetLinks(
        @Url assetLinkUrl: String,
    ): List<AssetLink>
}

data class AssetLink(
    val relation: List<String>,
    val target: AssetLinkTarget,
)

data class AssetLinkTarget(
    val namespace: String,
    val package_name: String?,
    val sha256_cert_fingerprints: List<String>?,

)
