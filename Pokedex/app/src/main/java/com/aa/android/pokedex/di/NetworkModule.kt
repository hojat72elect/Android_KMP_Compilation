package com.aa.android.pokedex.di

import android.content.Context
import com.aa.android.pokedex.api.PokemonApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideBaseUrl(): String = "https://pokeapi.co/api/v2/"

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    fun provideRetrofit(baseUrl: String, moshi: Moshi,@ApplicationContext context: Context): Retrofit {

        // Define the caching behavior
        val cacheDirectory = File(context.cacheDir, "http_cache")
        val cacheSize = 10 * 1024 * 1024L // 10 MB
        val cache = Cache(cacheDirectory, cacheSize)

        val okHttpClient = OkHttpClient.Builder()
            .cache(cache)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    fun providePokemonApi(retrofit: Retrofit): PokemonApi {
        return retrofit.create(PokemonApi::class.java)
    }
}