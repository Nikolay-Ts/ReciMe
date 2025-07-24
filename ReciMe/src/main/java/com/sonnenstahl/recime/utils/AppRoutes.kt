package com.sonnenstahl.recime.utils

sealed class AppRoutes(
    val route: String,
) {
    object Home : AppRoutes("home")

    object RecipeFinder : AppRoutes("recipeFinder")

    object Fridge : AppRoutes("fridge")

    object Recipes : AppRoutes("recipes")

    object Recipe : AppRoutes("recipe")

    object CouldNotLoad : AppRoutes("CouldNotLoad")
}
