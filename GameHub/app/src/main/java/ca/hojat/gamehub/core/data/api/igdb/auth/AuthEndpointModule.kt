package ca.hojat.gamehub.core.data.api.igdb.auth

import ca.hojat.gamehub.core.data.api.common.asConverterFactory
import ca.hojat.gamehub.core.data.api.common.calladapter.ApiResultCallAdapterFactory
import ca.hojat.gamehub.core.data.api.igdb.common.TwitchConstantsProvider
import ca.hojat.gamehub.core.data.api.igdb.common.di.qualifiers.Endpoint
import ca.hojat.gamehub.core.data.api.igdb.common.di.qualifiers.IgdbApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthEndpointModule {

    @Provides
    @Singleton
    fun provideAuthEndpoint(
        authService: AuthService,
        twitchConstantsProvider: TwitchConstantsProvider
    ): AuthEndpoint {
        return AuthEndpointImpl(
            authService = authService,
            clientId = twitchConstantsProvider.clientId,
            clientSecret = twitchConstantsProvider.clientSecret
        )
    }

    @Provides
    fun provideAuthService(@Endpoint(Endpoint.Type.AUTH) retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Endpoint(Endpoint.Type.AUTH)
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        @IgdbApi callAdapterFactory: ApiResultCallAdapterFactory,
        json: Json,
        twitchConstantsProvider: TwitchConstantsProvider
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .addCallAdapterFactory(callAdapterFactory)
            .addConverterFactory(json.asConverterFactory())
            .baseUrl(twitchConstantsProvider.apiBaseUrl)
            .build()
    }
}
