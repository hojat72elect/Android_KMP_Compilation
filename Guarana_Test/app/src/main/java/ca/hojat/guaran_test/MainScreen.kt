package ca.hojat.guaran_test

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ca.hojat.guaran_test.feature_favorites.FavoritesScreen
import ca.hojat.guaran_test.feature_home.HomeScreen
import ca.hojat.guaran_test.shared.NavigationBar

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(modifier)
            NavigationBar(navController)
        }
        composable("favorites") {
            FavoritesScreen(modifier)
            NavigationBar(navController)
        }
    }
}