package ca.hojat.guaran_test.feature_favorites

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ca.hojat.guaran_test.R
import ca.hojat.guaran_test.feature_home.CoffeeShop

@Composable
fun FavoritesScreen(modifier: Modifier = Modifier) {

    val favoriteCoffeeShops = listOf(
        CoffeeShop(
            name = "Place 1",
            isOpen = true,
            imageResId = R.drawable.list_icon_placeholder,
            distance = 3,
            description = "Single origin coffees, latte art & homemade goods lure locals to this snug, retro-industrial outfit."
        ),
        CoffeeShop(
            "Place 2",
            true,
            R.drawable.list_icon_placeholder,
            distance = 10,
            description = "Single origin coffees, latte art & homemade goods lure locals to this snug, retro-industrial outfit."
        ),
    )


    // list of coffee shops
    LazyColumn(modifier = modifier.padding(top = 60.dp)) {
        items(favoriteCoffeeShops) { shop ->
            FavoriteShopItem(shop)
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.LightGray)
            )
        }
    }
}


@Composable
fun FavoriteShopItem(shop: CoffeeShop) {

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
                text = "${shop.distance}Km away",
                fontSize = 16.sp,
                color = Color.Red
            )
        }

    }

    Text(text = shop.description, modifier = Modifier.padding(18.dp), color = Color.DarkGray, fontSize = 16.sp)
}