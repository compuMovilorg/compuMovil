package com.example.myapplication.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Text
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.myapplication.data.GastroBar
import com.example.myapplication.data.local.LocalGastroBarProvider
import com.example.myapplication.data.local.LocalReviewsProvider
import com.example.myapplication.ui.*

sealed class Screen(val route: String) {
    object Start : Screen("start")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Search : Screen("search")
    object Profile : Screen("profile")
    object Create : Screen("create")
    object Notification : Screen("notification")

  //  object Detail : Screen("detail/{gastroBarId}")

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
                modifier = modifier,
                onReviewClick = { reviewId ->
                    val review = LocalReviewsProvider.Reviews.find { it.id == reviewId }
                    review?.let {
                        navController.navigate("detail/${it.gastroBarId}")
                    }
                }
            )
        }
        composable("search") {
            SearchScreen(
                gastroBars = LocalGastroBarProvider.gastroBars,
                modifier = modifier

            )
        }
        composable("create") {
//            create(
//                modifier = modifier
//            )
        }
        composable("notification") {
//            detail(
//                modifier = modifier
//            )
        }
        composable("profile") {
            ProfileScreen(
                modifier = modifier
            )
        }
        composable(
            route = "detail/{gastroBarId}",
            arguments = listOf(navArgument("gastroBarId") { type = NavType.IntType })
        ){
            val gastroBarId = it.arguments?.getInt("gastroBarId") ?: 0
            val gastroBar = LocalGastroBarProvider.gastroBars.find { it.id == gastroBarId }
            if(gastroBar != null) {
                DetailGastroBarScreen(
                    gastroBar = gastroBar,
                    modifier = modifier
                )
            }else {
               Text(text = "GastroBar no encontrado")
            }
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
    BottomNavItem(Icons.Filled.Search, Icons.Outlined.Search, Screen.Search.route),
    BottomNavItem(Icons.Filled.AddCircle, Icons.Outlined.AddCircle, Screen.Create.route),
    BottomNavItem(Icons.Filled.Favorite, Icons.Outlined.Favorite, Screen.Notification.route),
    BottomNavItem(Icons.Filled.Person, Icons.Outlined.Person, Screen.Profile.route)
)