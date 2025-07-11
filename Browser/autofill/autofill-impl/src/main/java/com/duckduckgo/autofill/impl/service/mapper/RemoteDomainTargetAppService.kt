

package com.duckduckgo.autofill.impl.service.mapper

import com.duckduckgo.anvil.annotations.ContributesServiceApi
import com.duckduckgo.di.scopes.AppScope
import retrofit2.http.GET

@ContributesServiceApi(AppScope::class)
interface RemoteDomainTargetAppService {
    @GET("https://staticcdn.duckduckgo.com/android/domain-app-mapping.json")
    suspend fun fetchDataset(): RemoteDomainTargetDataSet
}

data class RemoteDomainTargetDataSet(
    val version: Long,
    val targets: List<RemoteDomainTarget>,
)

data class RemoteDomainTarget(
    val url: String,
    val apps: List<RemoteTargetApp>,
)

data class RemoteTargetApp(
    val package_name: String,
    val sha256_cert_fingerprints: List<String>,
)
