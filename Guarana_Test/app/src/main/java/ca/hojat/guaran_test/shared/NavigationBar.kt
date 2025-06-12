package ca.hojat.guaran_test.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ca.hojat.guaran_test.R

@Composable
fun NavigationBar(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .background(Color.Red)
            .padding(top = 30.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Box(
            modifier = Modifier
                .weight(1f)
            ,
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_navbar), // Replace with your logo
                contentDescription = "Logo",
                modifier = Modifier.size(150.dp)
            )
        }

        // like button
        IconButton(
            onClick = {
                navController.navigate("favorites")
            },
        ) {
            Image(
                painter = painterResource(id = R.drawable.heart_filled),
                contentDescription = "like button",
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 10.dp)
            )
        }


    }
}