package com.sonnenstahl.recime

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sonnenstahl.recime.utils.AppRoutes

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppRoutes.Home.route) {
        composable(AppRoutes.Home.route) {
            Home(navigation = navController)
        }

        composable("${AppRoutes.Recipe.route}/{name}") { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name")
            Recipe(navController = navController, name = name)
        }

        composable(AppRoutes.RecipeFinder.route) {
            RecipeFinder(navController = navController)
        }

        composable("${AppRoutes.Recipes.route}/{isRecipeFinder}/{mealName}") { backStackEntry ->
            val isRecipeFinder = backStackEntry.arguments?.getString("isRecipeFinder") == "true"
            val mealName = backStackEntry.arguments?.getString("mealName")
            Log.d("HOW AM I HERE", "$isRecipeFinder")
            Recipes(
                navController = navController,
                isRecipeFinder = isRecipeFinder,
                mealName = mealName,
            )
        }

        composable(AppRoutes.CouldNotLoad.route) {
            CouldNotLoad(navController = navController)
        }

    }
}
