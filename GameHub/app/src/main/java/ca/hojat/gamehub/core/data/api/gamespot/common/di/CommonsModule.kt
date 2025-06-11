package ca.hojat.gamehub.core.data.api.gamespot.common.di

import ca.hojat.gamehub.core.data.api.common.ErrorMessageExtractor
import ca.hojat.gamehub.core.data.api.common.addInterceptorAsFirstInChain
import ca.hojat.gamehub.core.data.api.common.calladapter.ApiResultCallAdapterFactory
import ca.hojat.gamehub.core.data.api.gamespot.common.GamespotConstantsProvider
import ca.hojat.gamehub.core.data.api.gamespot.common.GamespotQueryParamsFactory
import ca.hojat.gamehub.core.data.api.gamespot.common.GamespotQueryParamsFactoryImpl
import ca.hojat.gamehub.core.data.api.gamespot.common.UserAgentInterceptor
import ca.hojat.gamehub.core.data.api.gamespot.common.serialization.GamespotFieldsSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonsModule {

    @Provides
    @Singleton
    @GamespotApi
    fun provideOkHttpClient(
        okHttpClient: OkHttpClient,
        userAgentInterceptor: UserAgentInterceptor
    ): OkHttpClient {
        return okHttpClient.newBuilder()
            .addInterceptorAsFirstInChain(userAgentInterceptor)
            .build()
    }

    @Provides
    @GamespotApi
    fun provideApiResultCallAdapterFactory(
        @GamespotApi errorMessageExtractor: ErrorMessageExtractor
    ): ApiResultCallAdapterFactory {
        return ApiResultCallAdapterFactory(errorMessageExtractor)
    }

    @Provides
    fun provideGamespotQueryParamsBuilder(
        gamespotFieldsSerializer: GamespotFieldsSerializer,
        gamespotConstantsProvider: GamespotConstantsProvider
    ): GamespotQueryParamsFactory {
        return GamespotQueryParamsFactoryImpl(
            gamespotFieldsSerializer = gamespotFieldsSerializer,
            apiKey = gamespotConstantsProvider.apiKey
        )
    }
}
