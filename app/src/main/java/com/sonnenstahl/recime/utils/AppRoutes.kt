package com.sonnenstahl.recime.utils

sealed class AppRoutes(
    val route: String
) {
    object Home : AppRoutes("home")

    object RecipeFinder : AppRoutes("recipeFinder")

    object RandomRecipe : AppRoutes("randomRecipe")
}