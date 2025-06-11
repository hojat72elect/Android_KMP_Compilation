

package com.duckduckgo.pir.internal.service

import com.duckduckgo.app.global.api.ApiInterceptorPlugin
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.subscriptions.api.Subscriptions
import com.squareup.anvil.annotations.ContributesMultibinding
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import retrofit2.Invocation

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = ApiInterceptorPlugin::class,
)
class PirAuthInterceptor @Inject constructor(
    private val subscriptions: Subscriptions,
) : ApiInterceptorPlugin, Interceptor {
    override fun getInterceptor(): Interceptor = this

    override fun intercept(chain: Chain): Response {
        val request = chain.request()

        val authRequired = request.tag(Invocation::class.java)
            ?.method()
            ?.isAnnotationPresent(AuthRequired::class.java) == true

        return if (authRequired) {
            val accessToken = runBlocking { subscriptions.getAccessToken() }
                ?: throw IOException("Can't obtain access token for requestCan't obtain access token for request")

            val authenticatedRequest = request.newBuilder()
                .header("Authorization", "bearer $accessToken")
                .build()

            chain.proceed(authenticatedRequest)
        } else {
            chain.proceed(request)
        }
    }
}

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class AuthRequired
