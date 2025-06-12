package ca.hojat.guaran_test.feature_home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import ca.hojat.guaran_test.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var currentLocation by remember { mutableStateOf<Location?>(null) }
    var isPermissionGranted by remember { mutableStateOf(false) }

    val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        isPermissionGranted = isGranted
    }

    val coffeeShops = listOf(
        CoffeeShop("Pikolo", true, R.drawable.list_icon_placeholder),
        CoffeeShop("Pikolo", true, R.drawable.list_icon_placeholder),
        CoffeeShop("Pikolo", true, R.drawable.list_icon_placeholder),

        )

    LaunchedEffect(key1 = true) {
        // First check if location permission is granted
        if (checkLocationPermission(context)) {
            isPermissionGranted = true
        } else {
            // Ask for permission
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // Get current location
    LaunchedEffect(key1 = isPermissionGranted) {
        if (isPermissionGranted) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        currentLocation = it
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("HomeScreen", "Error getting location", e)
                }
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        // Map view (needs to be worked on later)
        if (isPermissionGranted && currentLocation != null) {
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(
                    LatLng(currentLocation!!.latitude, currentLocation!!.longitude),
                    15f
                )
            }
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    state = MarkerState(
                        position = LatLng(
                            currentLocation!!.latitude,
                            currentLocation!!.longitude
                        )
                    ),
                    title = "Your Location",
                    snippet = "You are here"
                )
            }
        } else {
            Text(text = "Location permission not granted or location not available")
        }

        // list of coffee shops
        LazyColumn {
            items(coffeeShops) { shop ->
                CoffeeShopItem(shop = shop)
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.LightGray)
                )
            }
        }
    }

}

private fun checkLocationPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}