package com.example.myapplication.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import com.example.myapplication.ui.*

sealed class Screen(val route: String) {
    object Start : Screen("start")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object DetailGastroBar : Screen("search")
    object Profile : Screen("profile")
}
@Composable

fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController

) {
    NavHost(
        navController = navController,
        startDestination = "start",
        modifier = modifier

    ) {
        composable("start") {
            StartScreen(
                LoginButtonPressd = { navController.navigate("login") },
                RegisterButtonPressd = { navController.navigate("register") },
                modifier = modifier
            )
        }

        composable("login") {
            LoginScreen(
                modifier = modifier
            )
        }
        composable("register") {
            RegisterScreen(
                modifier = modifier,
                RegisterButtomPressed = {
                    navController.navigate("home") {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable("home") {
            HomeScreen(
                modifier = modifier
            )
        }
        composable("search") {
//            SearchScreen(
//                modifier = modifier
//            )
        }
        composable("profile") {
            ProfileScreen(
                modifier = modifier
            )
        }

    }
}

data class BottomNavItem(
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector,
    val route: String
)


val bottomNavItems = listOf(
    BottomNavItem(Icons.Filled.Home, Icons.Outlined.Home, Screen.Home.route),
   // BottomNavItem(Icons.Filled.Search, Icons.Outlined.Search, Screen.Search.route),
    BottomNavItem(Icons.Filled.Person, Icons.Outlined.Person, Screen.Profile.route)
)