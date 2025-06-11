

package com.duckduckgo.subscriptions.impl.auth

import com.duckduckgo.common.utils.plugins.pixel.PixelInterceptorPlugin
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.subscriptions.impl.AccessTokenResult.Success
import com.duckduckgo.subscriptions.impl.SubscriptionsManager
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
    boundType = PixelInterceptorPlugin::class,
)
class AuthInterceptor @Inject constructor(
    private val subscriptionsManager: SubscriptionsManager,
) : PixelInterceptorPlugin, Interceptor {
    override fun getInterceptor(): Interceptor = this

    override fun intercept(chain: Chain): Response {
        val request = chain.request()

        val authRequired = request.tag(Invocation::class.java)
            ?.method()
            ?.isAnnotationPresent(AuthRequired::class.java) == true

        return if (authRequired) {
            val accessToken = getAccessToken()
                ?: throw IOException("Can't obtain access token for request")

            val authenticatedRequest = request.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()

            chain.proceed(authenticatedRequest)
        } else {
            chain.proceed(request)
        }
    }

    private fun getAccessToken(): String? {
        val accessTokenResult = runBlocking { subscriptionsManager.getAccessToken() }
        return (accessTokenResult as? Success)?.accessToken?.takeIf { it.isNotBlank() }
    }
}

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class AuthRequired
