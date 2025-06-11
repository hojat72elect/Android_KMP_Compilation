package com.aa.android.pokedex.api

import com.aa.android.pokedex.api.entity.PokemonDTO
import com.aa.android.pokedex.api.entity.ResourceListDTO
import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Main Retrofit API interface. There are 3 sets of APIs with the same functionality but different adapters:
 * - Coroutines
 * - Normal Retrofit Calls (no adapter)
 * - RxAndroid/RxJava
 */
interface PokemonApi {

    // Standard Retrofit Call APIs
    @GET("pokemon")
    fun getAllPokemonCall(@Query(value = "limit") limit: Int? = null, @Query(value = "offset") offset: Int? = null): Call<ResourceListDTO>

    @GET("pokemon/{id}")
    suspend fun getPokemonCall(@Path(value = "id") id: Int): Call<PokemonDTO>

    @GET("pokemon/{name}")
    suspend fun getPokemonCall(@Path(value = "name") name: String): Call<PokemonDTO>


    // Coroutine APIs
    @GET("pokemon")
    suspend fun getAllPokemon(@Query(value = "limit") limit: Int? = null, @Query(value = "offset") offset: Int? = null): Response<ResourceListDTO>

    @GET("pokemon/{id}")
    suspend fun getPokemon(@Path(value = "id") id: Int): Response<PokemonDTO>

    @GET("pokemon/{name}")
    suspend fun getPokemon(@Path(value = "name") name: String): Response<PokemonDTO>


    // Rx APIs
    @GET("pokemon")
    fun getAllPokemonRx(@Query(value = "limit") limit: Int? = null, @Query(value = "offset") offset: Int? = null): Observable<ResourceListDTO>

    @GET("pokemon/{id}")
    fun getPokemonRx(@Path(value = "id") id: Int): Observable<PokemonDTO>

    @GET("pokemon/{name}")
    fun getPokemonRx(@Path(value = "name") name: String): Observable<PokemonDTO>
}