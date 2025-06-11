package com.aa.android.pokedex.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.aa.android.pokedex.api.entity.PokemonDTO
import com.aa.android.pokedex.model.UiState
import com.aa.android.pokedex.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: PokemonRepository) : ViewModel() {

     fun getChosenPokemonLiveData(chosenPokemon:String): LiveData<UiState<PokemonDTO>> = liveData(Dispatchers.IO) {
        emit(UiState.Loading())
        try {
            val data = repository.getSinglePokemon(chosenPokemon)
        if (data!=null){
            emit(UiState.Ready(data))
        }else {
            emit(UiState.Error(Exception("The result of API call is null")))
        }
        }catch (e:Exception){
            Log.e(this@DetailsViewModel::class.simpleName, e.message, e)
            emit(UiState.Error(e))
        }
    }
}