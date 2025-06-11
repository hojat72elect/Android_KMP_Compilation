

package com.duckduckgo.networkprotection.impl.configuration

import com.duckduckgo.app.global.api.ApiInterceptorPlugin
import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.appbuildconfig.api.isInternalBuild
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.networkprotection.impl.BuildConfig
import com.duckduckgo.subscriptions.api.Subscriptions
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import logcat.logcat
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import retrofit2.Invocation

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = ApiInterceptorPlugin::class,
)
class NetpControllerRequestInterceptor @Inject constructor(
    private val appBuildConfig: AppBuildConfig,
    private val subscriptions: Subscriptions,
) : ApiInterceptorPlugin, Interceptor {

    override fun getInterceptor(): Interceptor = this

    override fun intercept(chain: Chain): Response {
        val request = chain.request()
        val url = request

        val authRequired = chain.request().tag(Invocation::class.java)
            ?.method()
            ?.isAnnotationPresent(AuthRequired::class.java) == true

        return if (authRequired) {
            val newRequest = chain.request().newBuilder()

            logcat { "Adding Authorization Bearer token to request $url" }
            newRequest.addHeader(
                name = "Authorization",
                // this runBlocking is fine as we're already in a background thread
                value = runBlocking { authorizationHeaderValue() },
            )

            if (appBuildConfig.isInternalBuild()) {
                newRequest.addHeader(
                    name = "NetP-Debug-Code",
                    value = BuildConfig.NETP_DEBUG_SERVER_TOKEN,
                )
            }

            chain.proceed(
                newRequest.build().also { logcat { "headers: ${it.headers}" } },
            )
        } else {
            chain.proceed(request)
        }
    }

    private suspend fun authorizationHeaderValue(): String {
        return "bearer ddg:${subscriptions.getAccessToken()}"
    }
}
