package com.weather.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.weather.screens.main.MainViewModel
import com.weather.screens.main.WeatherScreen
import com.weather.screens.search.FavouriteViewModel
import com.weather.screens.search.SearchScreen
import com.weather.screens.splash.SplashScreen
import com.weather.widgets.BottomNavItem

@Composable
fun WeatherNavigation(context: Context) {
    val navController = rememberNavController()
    val mainViewModel = hiltViewModel<MainViewModel>()
    val favouriteViewModel = hiltViewModel<FavouriteViewModel>()

    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        val route = BottomNavItem.Home.route
        composable("$route/{city}", arguments = listOf(navArgument(name = "city") {
            type = NavType.StringType
        })) { navBack ->
            navBack.arguments?.getString("city").let { city ->
                WeatherScreen(
                    navController = navController,
                    mainViewModel = mainViewModel,
                    context = context,
                    city = city
                )
            }
        }
        composable(BottomNavItem.Search.route) {
            SearchScreen(navController = navController, context, favouriteViewModel, mainViewModel)
        }
    }
}