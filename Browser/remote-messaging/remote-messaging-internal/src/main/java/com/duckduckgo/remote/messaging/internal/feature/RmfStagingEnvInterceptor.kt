

package com.duckduckgo.remote.messaging.internal.feature

import com.duckduckgo.app.global.api.ApiInterceptorPlugin
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.remote.messaging.internal.setting.RmfInternalSettings
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject
import logcat.logcat
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = ApiInterceptorPlugin::class,
)
class RmfStagingEnvInterceptor @Inject constructor(
    private val rmfInternalSettings: RmfInternalSettings,
) : ApiInterceptorPlugin, Interceptor {
    override fun getInterceptor(): Interceptor = this
    override fun intercept(chain: Chain): Response {
        val lastSegment = chain.request().url.encodedPathSegments.last()

        if (rmfInternalSettings.useStatingEndpoint().isEnabled() && chain.request().url.isProductionEnvironment()) {
            val newRequest = chain.request().newBuilder()

            val changedUrl = RMF_STAGING_ENV + lastSegment
            logcat { "RMF environment changed to $changedUrl" }
            newRequest.url(changedUrl)
            return chain.proceed(newRequest.build())
        }

        return chain.proceed(chain.request())
    }

    private fun HttpUrl.isProductionEnvironment(): Boolean {
        return this.toString().let {
            it.contains(RMF_REQUEST) && !it.contains(RMF_STAGING_ENV)
        }
    }

    companion object {
        private const val RMF_REQUEST = "https://staticcdn.duckduckgo.com/remotemessaging/"
        private const val RMF_STAGING_ENV = "https://staticcdn.duckduckgo.com/remotemessaging/config/staging/"
    }
}
