package ca.hojat.guaran_test.feature_home

data class CoffeeShop(
    val name: String,
    val isOpen: Boolean,
    val imageResId: Int,
    val distance: Int = 3,
    val description: String = ""
)
