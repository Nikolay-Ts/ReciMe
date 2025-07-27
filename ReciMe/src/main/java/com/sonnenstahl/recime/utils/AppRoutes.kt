package com.sonnenstahl.recime.utils

/**
 * used to manage all of the navigation routes and also acts as a skeleton of the app to quickly
 * display all of the main pages
 *
 * @property route the string that is used to navigate to the specific views
 */
sealed class AppRoutes(
    val route: String,
) {
    /**
     * links to the [Home] view
     */
    object Home : AppRoutes("home")

    /**
     * links to the [RecipeFinder] view
     */
    object RecipeFinder : AppRoutes("recipeFinder")

    /**
     * links to the [Fridge] view
     */
    object Fridge : AppRoutes("fridge")

    /**
     * links to the [] view
     */
    object AddIngredients : AppRoutes("addIngredients")

    /**
     * links to the [Recipes] view
     */
    object Recipes : AppRoutes("recipes")

    /**
     * links to the [Recipe] view
     */
    object Recipe : AppRoutes("recipe")

    /**
     * links to the [CouldNotLoad] view
     */
    object CouldNotLoad : AppRoutes("CouldNotLoad")
}
