package ca.hojat.guaran_test.feature_home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CoffeeShopItem(shop: CoffeeShop) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Coffee Shop Image
        Image(
            painter = painterResource(id = shop.imageResId),
            contentDescription = "Coffee Shop Image",
            modifier = Modifier
                .size(48.dp)
                .padding(start = 10.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Coffee Shop Name
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = shop.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Red
            )
        }

        // Open/Closed Status
        Column {
            Text(
                text = if (shop.isOpen) "OPEN" else "CLOSED",
                fontSize = 16.sp,
                color = Color.Green
            )
            Text(
                text = "${shop.distance}KM away",
                fontSize = 16.sp,
                color = Color.Red
            )
        }

    }
}