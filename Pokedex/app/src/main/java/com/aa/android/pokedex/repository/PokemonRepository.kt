package com.aa.android.pokedex.repository

import com.aa.android.pokedex.api.PokemonApi
import com.aa.android.pokedex.api.entity.PokemonDTO
import javax.inject.Inject

class PokemonRepository @Inject constructor(val api: PokemonApi) {

    /**
     * Returns a pair. First item of the pair is total number of pokemons that are available
     * in API service. Second item of the pair is a list of first page of pokemons we have
     * loaded from API service.
     */
    suspend fun getAllPokemon(): Pair<Int,List<String>> {
        val response = api.getAllPokemon()
        if (response.isSuccessful) {
            response.body()?.let {
                return Pair(it.count ,it.results.map { result ->
                    result.name
                })
            }
        }
        return Pair(0 ,listOf())
    }

    suspend fun getSinglePokemon(name: String): PokemonDTO? {
        val response = api.getPokemon(name)
        return if (response.isSuccessful)
            response.body()
        else
            null
    }
}