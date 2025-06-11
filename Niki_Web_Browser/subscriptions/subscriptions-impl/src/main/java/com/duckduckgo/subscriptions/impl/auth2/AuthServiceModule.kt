

package com.duckduckgo.subscriptions.impl.auth2

import android.annotation.SuppressLint
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn
import javax.inject.Named
import javax.inject.Provider
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
@ContributesTo(AppScope::class)
object AuthServiceModule {
    @SuppressLint("NoRetrofitCreateMethodCallDetector")
    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideAuthService(
        @Named("nonCaching") okHttpClient: Provider<OkHttpClient>,
        @Named("nonCaching") retrofit: Retrofit,
    ): AuthService {
        val okHttpClientWithoutRedirects = lazy {
            okHttpClient.get().newBuilder()
                .followRedirects(false)
                .build()
        }

        return retrofit.newBuilder()
            .callFactory { okHttpClientWithoutRedirects.value.newCall(it) }
            .build()
            .create(AuthService::class.java)
    }
}
