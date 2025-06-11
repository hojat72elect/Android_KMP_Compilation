package ca.hojat.gamehub.core.data.api.igdb.common.di

import ca.hojat.gamehub.core.data.api.common.ErrorMessageExtractor
import ca.hojat.gamehub.core.data.api.common.addInterceptorAsFirstInChain
import ca.hojat.gamehub.core.data.api.common.calladapter.ApiResultCallAdapterFactory
import ca.hojat.gamehub.core.data.api.igdb.auth.Authorizer
import ca.hojat.gamehub.core.data.api.igdb.common.AuthorizationInterceptor
import ca.hojat.gamehub.core.data.api.igdb.common.CredentialsStore
import ca.hojat.gamehub.core.data.api.igdb.common.TwitchConstantsProvider
import ca.hojat.gamehub.core.data.api.igdb.common.di.qualifiers.IgdbApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonsModule {

    @Provides
    @Singleton
    @IgdbApi
    fun provideOkHttpClient(
        okHttpClient: OkHttpClient,
        authorizationInterceptor: AuthorizationInterceptor,
        authenticator: Authenticator,
    ): OkHttpClient {
        return okHttpClient.newBuilder()
            .addInterceptorAsFirstInChain(authorizationInterceptor)
            .authenticator(authenticator)
            .build()
    }

    @Provides
    @IgdbApi
    fun provideApiResultCallAdapterFactory(
        @IgdbApi errorMessageExtractor: ErrorMessageExtractor
    ): ApiResultCallAdapterFactory {
        return ApiResultCallAdapterFactory(errorMessageExtractor)
    }

    @Provides
    fun provideAuthorizationInterceptor(
        credentialsStore: CredentialsStore,
        authorizer: Authorizer,
        twitchConstantsProvider: TwitchConstantsProvider
    ): AuthorizationInterceptor {
        return AuthorizationInterceptor(
            credentialsStore = credentialsStore,
            authorizer = authorizer,
            clientId = twitchConstantsProvider.clientId
        )
    }
}
