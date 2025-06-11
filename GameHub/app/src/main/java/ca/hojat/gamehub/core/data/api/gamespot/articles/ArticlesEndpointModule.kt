package ca.hojat.gamehub.core.data.api.gamespot.articles

import ca.hojat.gamehub.core.data.api.common.asConverterFactory
import ca.hojat.gamehub.core.data.api.common.calladapter.ApiResultCallAdapterFactory
import ca.hojat.gamehub.core.data.api.gamespot.common.GamespotConstantsProvider
import ca.hojat.gamehub.core.data.api.gamespot.common.di.Endpoint
import ca.hojat.gamehub.core.data.api.gamespot.common.di.GamespotApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ArticlesEndpointModule {

    @Provides
    fun provideArticlesService(@Endpoint(Endpoint.Type.ARTICLES) retrofit: Retrofit): ArticlesService {
        return retrofit.create(ArticlesService::class.java)
    }

    @Provides
    @Endpoint(Endpoint.Type.ARTICLES)
    fun provideRetrofit(
        @GamespotApi okHttpClient: OkHttpClient,
        @GamespotApi callAdapterFactory: ApiResultCallAdapterFactory,
        json: Json,
        gamespotConstantsProvider: GamespotConstantsProvider
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .addCallAdapterFactory(callAdapterFactory)
            .addConverterFactory(json.asConverterFactory())
            .baseUrl(gamespotConstantsProvider.apiBaseUrl)
            .build()
    }
}
