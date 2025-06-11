

package com.duckduckgo.sync.impl.internal

import com.duckduckgo.app.global.api.ApiInterceptorPlugin
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.sync.impl.SyncService.Companion.SYNC_DEV_ENVIRONMENT_URL
import com.duckduckgo.sync.impl.SyncService.Companion.SYNC_PROD_ENVIRONMENT_URL
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import timber.log.Timber

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = ApiInterceptorPlugin::class,
)
class SyncInternalEnvInterceptor @Inject constructor(
    private val envDataStore: SyncInternalEnvDataStore,
) : ApiInterceptorPlugin, Interceptor {
    override fun getInterceptor(): Interceptor = this
    override fun intercept(chain: Chain): Response {
        val useDevEnvironment = envDataStore.useSyncDevEnvironment

        if (useDevEnvironment && chain.request().url.toString().contains(SYNC_PROD_ENVIRONMENT_URL)) {
            val newRequest = chain.request().newBuilder()

            val changedUrl = chain.request().url.toString().replace(SYNC_PROD_ENVIRONMENT_URL, SYNC_DEV_ENVIRONMENT_URL)
            Timber.d("Sync-Engine: environment changed to $changedUrl")
            newRequest.url(changedUrl)
            return chain.proceed(newRequest.build())
        }

        return chain.proceed(chain.request())
    }
}
