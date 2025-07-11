

package com.duckduckgo.sync.impl.engine

import com.duckduckgo.app.global.api.ApiInterceptorPlugin
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.sync.impl.API_CODE
import com.duckduckgo.sync.impl.SyncService
import com.duckduckgo.sync.impl.error.SyncUnavailableRepository
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = ApiInterceptorPlugin::class,
)
class SyncServerUnavailableInterceptor @Inject constructor(
    private val syncUnavailableRepository: SyncUnavailableRepository,
) : ApiInterceptorPlugin, Interceptor {

    override fun intercept(chain: Chain): Response {
        val isSyncEndpoint = chain.request().url.toString().contains(SyncService.SYNC_PROD_ENVIRONMENT_URL) ||
            chain.request().url.toString().contains(SyncService.SYNC_DEV_ENVIRONMENT_URL)

        if (!isSyncEndpoint) {
            return chain.proceed(chain.request())
        }

        val response = chain.proceed(chain.request())
        val method = chain.request().method
        if (method == "PATCH" || method == "GET") {
            when (response.code) {
                API_CODE.TOO_MANY_REQUESTS_1.code,
                API_CODE.TOO_MANY_REQUESTS_2.code,
                -> {
                    syncUnavailableRepository.onServerUnavailable()
                }
            }
        }

        if (response.isSuccessful) {
            syncUnavailableRepository.onServerAvailable()
        }

        return response
    }

    override fun getInterceptor() = this
}
