package com.sonnenstahl.recime

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sonnenstahl.recime.utils.AppRoutes
import com.sonnenstahl.recime.utils.data.SearchType

/**
 * the navigation graph which connects the URL to the composable function
 */
@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppRoutes.Home.route) {
        composable(AppRoutes.Home.route) {
            Home(navigation = navController)
        }

        composable("${AppRoutes.Recipe.route}/{name}") { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name")
            Recipe(navController = navController, mealName = name)
        }

        composable(AppRoutes.RecipeFinder.route) {
            RecipeFinder(navController = navController)
        }

        composable("${AppRoutes.Recipes.route}/{mealType}/{mealName}") { backStackEntry ->
            val searchTypeString = backStackEntry.arguments?.getString("mealType")
            val searchType = searchTypeString?.let { SearchType.valueOf(it) } ?: SearchType.NONE
            val mealName = backStackEntry.arguments?.getString("mealName")

            Recipes(
                navController = navController,
                searchType = searchType,
                mealName = mealName,
            )
        }

        composable(AppRoutes.Fridge.route) {
            Fridge(navController = navController)
        }

        composable(AppRoutes.AddIngredients.route) {
            AddIngredients(navController = navController)
        }

        composable(AppRoutes.CouldNotLoad.route) {
            CouldNotLoad(navController = navController)
        }
    }
}
