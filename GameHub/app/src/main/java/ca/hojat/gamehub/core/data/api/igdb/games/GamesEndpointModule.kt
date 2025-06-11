package ca.hojat.gamehub.core.data.api.igdb.games

import ca.hojat.gamehub.core.data.api.common.asConverterFactory
import ca.hojat.gamehub.core.data.api.common.calladapter.ApiResultCallAdapterFactory
import ca.hojat.gamehub.core.data.api.igdbcalypse.querybuilder.ApicalypseQueryBuilderFactory
import ca.hojat.gamehub.core.data.api.igdbcalypse.serialization.ApicalypseSerializer
import ca.hojat.gamehub.core.data.api.igdbcalypse.serialization.ApicalypseSerializerFactory
import ca.hojat.gamehub.core.data.api.igdb.common.IgdbConstantsProvider
import ca.hojat.gamehub.core.data.api.igdb.common.di.qualifiers.Endpoint
import ca.hojat.gamehub.core.data.api.igdb.common.di.qualifiers.IgdbApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object GamesEndpointModule {

    @Provides
    fun provideGamesService(@Endpoint(Endpoint.Type.GAMES) retrofit: Retrofit): GamesService {
        return retrofit.create(GamesService::class.java)
    }

    @Provides
    @Endpoint(Endpoint.Type.GAMES)
    fun provideRetrofit(
        @IgdbApi okHttpClient: OkHttpClient,
        @IgdbApi callAdapterFactory: ApiResultCallAdapterFactory,
        json: Json,
        igdbConstantsProvider: IgdbConstantsProvider
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .addCallAdapterFactory(callAdapterFactory)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(json.asConverterFactory())
            .baseUrl(igdbConstantsProvider.apiBaseUrl)
            .build()
    }

    @Provides
    fun provideApicalypseQueryBuilderFactory(): ApicalypseQueryBuilderFactory {
        return ApicalypseQueryBuilderFactory
    }

    @Provides
    fun provideApicalypseSerializer(): ApicalypseSerializer {
        return ApicalypseSerializerFactory.create()
    }
}
