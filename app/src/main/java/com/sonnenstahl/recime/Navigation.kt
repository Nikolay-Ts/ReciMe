package com.sonnenstahl.recime

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sonnenstahl.recime.utils.AppRoutes

@Composable
fun Navigation() {
    val context = LocalContext.current

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppRoutes.Home.route) {
        composable(AppRoutes.Home.route) {
            Home(navigation=navController)
        }

        composable(AppRoutes.Recipe.route) {
            Recipe()
        }

        composable(AppRoutes.Recipes.route) {
            Recipes(navController=navController)
        }
    }


}
