package com.example.myapplication.navigation

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.compose.material3.Text
import com.example.myapplication.data.local.LocalGastroBarProvider
import com.example.myapplication.data.local.LocalReviewsProvider
import com.example.myapplication.data.local.LocalEventsProvider
import com.example.myapplication.ui.*

sealed class Screen(val route: String) {
    object Start : Screen("start")
    object Login : Screen("login")
    object Register : Screen("register")
    object ResetPassword : Screen("resetPassword")
    object Home : Screen("home")
    object Detail : Screen("detail/{gastroBarId}")
    object Search : Screen("search")
    object Create : Screen("create")
    object Events : Screen("events")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
    object Notification : Screen("notification")
}

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Start.route,
        modifier = modifier
    ) {
        composable(Screen.Start.route) {
            StartScreen(
                LoginButtonPressd = { navController.navigate(Screen.Login.route) },
                RegisterButtonPressd = { navController.navigate(Screen.Register.route) },
                modifier = modifier
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                modifier = modifier,
                onLoginClick = { _, _ ->
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onForgotPasswordClick = {
                    navController.navigate(Screen.ResetPassword.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                modifier = modifier,
                RegisterButtomPressed = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.ResetPassword.route) {
            ResetPasswordScreen(
                modifier = modifier,
                onLogInClick = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                modifier = modifier,
                onReviewClick = { reviewId ->
                    val review = LocalReviewsProvider.Reviews.find { it.id == reviewId }
                    val idGastroBar = review?.gastroBarId ?: 0
                    Log.d("HomeScreen", "Review ID: $reviewId")
                    review?.let {
                        navController.navigate("detail/$idGastroBar")
                    }
                }
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                gastroBars = LocalGastroBarProvider.gastroBars,
                modifier = modifier,
                onGastroBarClick = { gastroBarId ->
                    navController.navigate("detail/$gastroBarId")
                }
            )
        }

        composable(Screen.Create.route) {
            CreateScreen(
                onSaveClick = { _, _, _ ->
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                modifier = modifier
            )
        }

        composable(Screen.Events.route) {
            EventScreen(
                modifier = modifier,
                events = LocalEventsProvider.events,
                onEventClick = {
                    navController.navigate("detail/${it.title}")
                }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                modifier = modifier,
                onConfiguracionClick = { navController.navigate(Screen.Settings.route) },
                onNotificationClick = { navController.navigate(Screen.Notification.route) }
            )
        }

        composable(Screen.Notification.route) {
            NotificationScreen(modifier = modifier)
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onLogoutClick = {
                    navController.navigate(Screen.Start.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "detail/{gastroBarId}",
            arguments = listOf(navArgument("gastroBarId") { type = NavType.IntType })
        ) {
            val gastroBarId = it.arguments?.getInt("gastroBarId") ?: 0
            val gastroBar = LocalGastroBarProvider.gastroBars.find { it.id == gastroBarId }
            if (gastroBar != null) {
                DetailGastroBarScreen(
                    gastroBar = gastroBar,
                    modifier = modifier
                )
            } else {
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
    BottomNavItem(Icons.Filled.Notifications, Icons.Outlined.Notifications, Screen.Events.route),
    BottomNavItem(Icons.Filled.Person, Icons.Outlined.Person, Screen.Profile.route)
)
