

package com.duckduckgo.malicioussiteprotection.impl.data.network

import com.duckduckgo.app.global.api.ApiInterceptorPlugin
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.malicioussiteprotection.impl.BuildConfig
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject
import logcat.logcat
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import retrofit2.Invocation

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = ApiInterceptorPlugin::class,
)
class MaliciousSiteProtectionRequestInterceptor @Inject constructor() : ApiInterceptorPlugin, Interceptor {
    override fun getInterceptor(): Interceptor = this

    override fun intercept(chain: Chain): Response {
        val request = chain.request()

        val authRequired = chain.request().tag(Invocation::class.java)
            ?.method()
            ?.isAnnotationPresent(AuthRequired::class.java) == true

        return if (authRequired) {
            val newRequest = chain.request().newBuilder()
            newRequest.addHeader(
                name = "X-Auth-Token",
                value = BuildConfig.MALICIOUS_SITE_PROTECTION_AUTH_TOKEN,
            )
            chain.proceed(
                newRequest.build().also { logcat { "headers: ${it.headers}" } },
            )
        } else {
            chain.proceed(request)
        }
    }
}
