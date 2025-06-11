package com.aa.android.pokedex.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.aa.android.pokedex.model.UiState
import com.aa.android.pokedex.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers

@HiltViewModel
class MainViewModel @Inject constructor(repository: PokemonRepository) : ViewModel() {

    val pokemonLiveData: LiveData<UiState<Pair<Int, List<String>>>> = liveData(Dispatchers.IO) {
        emit(UiState.Loading())
        try {
            val data = repository.getAllPokemon()
            emit(UiState.Ready(data))
        } catch (e: Exception) {
            Log.e(this@MainViewModel::class.simpleName, e.message, e)
            emit(UiState.Error(e))
        }
    }
}