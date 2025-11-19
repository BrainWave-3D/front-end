package com.example.brainwave3d.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
//    @Serializable
//    data object WishListScreen : Screen()
//
//    @Serializable
//    data object SellScreen : Screen()
//
//    @Serializable
//    data object CategorySelectScreen : Screen()
//
//    //Sell Screens
//    @Serializable
//    data object SearchScreen : Screen()
//    @Serializable
//    data class ProductListingScreen(
//        val query: String,
//        val priceType: String? = null,
//        val maxPriceCash: String? = null,
//        val maxPriceCoin: String? = null,
//        val primaryCategory: String? = null,
//        val secondaryCategory: String? = null,
//        val tertiaryCategory: String? = null,
//    ) : Screen()
//
//    @Serializable
//    data class FollowersScreen(val type: String?, val userId: String?) : Screen()

    @Serializable
    data object HomeScreen : Screen()

    @Serializable
    data object SignUpScreen : Screen()

    @Serializable
    data object LoginScreen : Screen()

    @Serializable
    data object UserDetailsScreen : Screen()

    @Serializable
    data object ProfileScreen : Screen()

}