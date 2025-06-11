package com.aa.android.pokedex.ui.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import coil.compose.rememberAsyncImagePainter
import com.aa.android.pokedex.api.entity.PokemonDTO
import com.aa.android.pokedex.model.Type
import com.aa.android.pokedex.model.UiState
import com.aa.android.pokedex.viewmodel.DetailsViewModel

@Composable
fun PokemonDetailScreen(viewModel: DetailsViewModel, pokemonName: String) {

    PokemonDetailScreen(viewModel.getChosenPokemonLiveData(pokemonName))
}

/**
 * Shows details for a specific pokemon which was chosen by the user.
 */
@Composable
private fun PokemonDetailScreen(pokemonLiveData: LiveData<UiState<PokemonDTO>>) {
    val uiState: UiState<PokemonDTO>? by pokemonLiveData.observeAsState()

    val scrollState = rememberScrollState()
    if (uiState is UiState.Ready<PokemonDTO>) {

        val uiStateData = (uiState as UiState.Ready<PokemonDTO>).data
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = rememberAsyncImagePainter(model = uiStateData.sprites.other.dreamWorldSprite.defaultFront),
                contentDescription = uiStateData.name,
                modifier = Modifier
                    .size(200.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = uiStateData.name.capitalize(Locale.current),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {

                // The "height" field is in decimeter so should be converted to centimeter and be shown accordingly
                Text("Height :  ${uiStateData.height * 10} Centimeters", modifier = Modifier.padding(vertical = 8.dp))
                // The "weight" is in hectogram so should be converted to kilogram and be shown accordingly
                Text("Weight :  ${uiStateData.weight / 10F} Kilograms", modifier = Modifier.padding(vertical = 8.dp))

                uiStateData.types.forEachIndexed { index, pokemonTypeDTO ->
                    Text(
                        text = "Type ${index + 1} :  ${pokemonTypeDTO.type.name}",
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .background(Type.getColor(pokemonTypeDTO.type.name.uppercase()))
                    )
                }

                for (pokemonStat in uiStateData.stats)
                    Text("${pokemonStat.stat.name} : ${pokemonStat.baseStat} - effort points : ${pokemonStat.effort}", modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}