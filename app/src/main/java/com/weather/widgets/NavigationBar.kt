package com.weather.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.weather.navigation.Screen
import com.weather.screens.main.MainViewModel

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(
        route = Screen.Home.route,
        icon = Icons.Outlined.Home
    )

    object Search : BottomNavItem(
        route = Screen.Search.route,
        icon = Icons.Outlined.Search
    )
}

@Composable
fun NavBar(navController: NavController, mainViewModel: MainViewModel) { // TODO DEFAULT
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search
    )
    BottomNavigation(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp, bottom = 15.dp, start = 15.dp, end = 15.dp)
            .clip(
                RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp)
            ),
        backgroundColor = colors.primaryVariant,
        contentColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            var color = Color.White.copy(alpha = 0.4f)
            BottomNavigationItem(
                icon = {
                    if (currentRoute == item.route) {
                        color = Color(0xFFd68118)
                    } else if (item == BottomNavItem.Home) {
                        if (currentRoute != BottomNavItem.Search.route) {
                            color = Color(0xFFd68118)
                        }
                    }
                    Icon(item.icon, contentDescription = null, tint = color)
                },
                selected = currentRoute == item.route,
                selectedContentColor = Color.Transparent,
                onClick = {
                    if (item != BottomNavItem.Home) {
                        navController.navigate(item.route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            // Avoid multiple copies of the same destination when re-selecting the same item
                            launchSingleTop = true
                            // Restore state when re-selecting a previously selected item
                            restoreState = true
                        }
                    } else {
                        // TODO DEFAULT
                        val lastCity = mainViewModel.city.value
                        navController.navigate(item.route + "/$lastCity") {
//                        navController.navigate(item.route + "/$defaultCity") {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            // Avoid multiple copies of the same destination when re-selecting the same item
                            launchSingleTop = true
                            // Restore state when re-selecting a previously selected item
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}