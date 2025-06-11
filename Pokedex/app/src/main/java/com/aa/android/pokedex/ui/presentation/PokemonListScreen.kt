package com.aa.android.pokedex.ui.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.navigation.NavHostController
import com.aa.android.pokedex.model.UiState
import com.aa.android.pokedex.viewmodel.MainViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer

@Composable
fun PokemonListScreen(viewModel: MainViewModel, navController: NavHostController) {
    PokemonListScreen(
        viewModel.pokemonLiveData,
        navController
    )
}

/**
 * Main Screen of the app.
 */
@Composable
private fun PokemonListScreen(pokemon: LiveData<UiState<Pair<Int, List<String>>>>, navController: NavHostController) {
    val uiState: UiState<Pair<Int, List<String>>>? by pokemon.observeAsState()
    Column {
        val numPokemons = (uiState as? UiState.Ready<Pair<Int, List<String>>>)?.data?.first ?: 0
        Text(text = "Total number of Pokemons : $numPokemons", modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 8.dp))
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {

            uiState?.let {
                when (it) {
                    is UiState.Loading -> {
                        items(20) {
                            PokemonItem(pokemon = "", isLoading = true, navController)
                        }
                    }

                    is UiState.Ready -> {
                        items(it.data.second) { pkmn ->
                            PokemonItem(pokemon = pkmn, isLoading = false, navController)
                        }
                    }

                    is UiState.Error -> {
                        item {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                textAlign = TextAlign.Center,
                                text = "Error loading list. Please try again later.",
                                color = MaterialTheme.colors.onBackground
                            )
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun PokemonItem(pokemon: String, isLoading: Boolean, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
            .placeholder(
                visible = isLoading,
                highlight = PlaceholderHighlight.shimmer(),
                shape = RoundedCornerShape(8.dp)
            ),
        shape = RoundedCornerShape(8.dp),
        onClick = {
            navController.navigate("pokemonDetail/${pokemon}")
        }) {
        Text(text = pokemon.capitalize(Locale.current), modifier = Modifier.padding(12.dp), textAlign = TextAlign.Center)
    }
}