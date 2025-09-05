package com.example.myapplication.navigation

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myapplication.data.local.LocalEventsProvider
import com.example.myapplication.data.local.LocalGastroBarProvider
import com.example.myapplication.data.local.LocalReviewsProvider
import com.example.myapplication.ui.create.CreateScreen
import com.example.myapplication.ui.create.CreateViewModel
import com.example.myapplication.ui.detailBar.DetailGastroBarScreen
import com.example.myapplication.ui.detailBar.DetailGastroBarViewModel
import com.example.myapplication.ui.events.EventScreen
import com.example.myapplication.ui.events.EventViewModel
import com.example.myapplication.ui.home.HomeScreen
import com.example.myapplication.ui.home.HomeViewModel
import com.example.myapplication.ui.log.LoginScreen
import com.example.myapplication.ui.log.LoginViewModel
import com.example.myapplication.ui.notification.NotificationScreen
import com.example.myapplication.ui.notification.NotificationViewModel
import com.example.myapplication.ui.profile.ProfileScreen
import com.example.myapplication.ui.profile.ProfileViewModel
import com.example.myapplication.ui.register.RegisterScreen
import com.example.myapplication.ui.register.RegisterViewModel
import com.example.myapplication.ui.resetPassword.ResetPasswordScreen
import com.example.myapplication.ui.resetPassword.ResetPasswordViewModel
import com.example.myapplication.ui.search.SearchScreen
import com.example.myapplication.ui.search.SearchViewModel
import com.example.myapplication.ui.settings.SettingsScreen
import com.example.myapplication.ui.settings.SettingsViewModel
import com.example.myapplication.ui.start.StartScreen

sealed class Screen(val route: String) {
    object StartRoute : Screen("start")
    object Login : Screen("login")
    object Register : Screen("register")
    object ResetPassword : Screen("resetPassword")
    object Home : Screen("home")
    object Detail : Screen("detail/{gastroBarId}")
    object Search : Screen("search")
    object Create : Screen("create")
    object Events : Screen("events")
    object Profile : Screen("profile")
    object SettingsRoute : Screen("settings")
    object Notification : Screen("notification")
}

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.StartRoute.route,
        modifier = modifier
    ) {
        composable(Screen.StartRoute.route) {
            StartScreen(
                onNavigateLogin = { navController.navigate(Screen.Login.route) },
                onNavigateRegister = { navController.navigate(Screen.Register.route) },
                modifier = modifier
            )
        }

        composable(Screen.Login.route) {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                modifier = modifier,
                viewModel = loginViewModel,
                onLoginSuccess = {
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
            val registerViewModel: RegisterViewModel = hiltViewModel()

            RegisterScreen(
                modifier = modifier,
                registerViewModel = registerViewModel,
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }




        composable(Screen.ResetPassword.route) {
            val resetPasswordViewModel: ResetPasswordViewModel = hiltViewModel()
            ResetPasswordScreen(
                modifier = modifier,
                onLogInClick = {
                    navController.navigate(Screen.Login.route)
                },
                viewModel = resetPasswordViewModel
            )
        }

        composable(Screen.Home.route) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                modifier = modifier,
                onReviewClick = { reviewId ->
                    val review = LocalReviewsProvider.Reviews.find { it.id == reviewId }
                    val idGastroBar = review?.gastroBarId ?: 0
                    Log.d("HomeScreen", "Review ID: $reviewId")
                    review?.let {
                        navController.navigate("detail/$idGastroBar")
                    }
                },
                viewModel = homeViewModel
            )
        }

        composable(Screen.Search.route) {
            val searchViewModel: SearchViewModel = hiltViewModel()
            SearchScreen(
                gastroBars = LocalGastroBarProvider.gastroBars,
                modifier = modifier,
                onGastroBarClick = { gastroBarId ->
                    navController.navigate("detail/$gastroBarId")
                },
                viewModel = searchViewModel
            )
        }

        composable(Screen.Create.route) {
            val createViewModel: CreateViewModel = hiltViewModel()
            CreateScreen(
                onSaveClick = { _, _, _ ->
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                modifier = modifier,
                viewModel = createViewModel
            )
        }

        composable(Screen.Events.route) {
            val eventViewModel: EventViewModel = hiltViewModel()
            EventScreen(
                modifier = modifier,
                onEventClick = { event -> },
                viewModel = eventViewModel
            )
        }

        composable(Screen.Profile.route) {
            val profileViewModel: ProfileViewModel = hiltViewModel()
            ProfileScreen(
                modifier = modifier,
                viewModel = profileViewModel,
                onConfiguracionClick = { navController.navigate(Screen.SettingsRoute.route) },
                onNotificationClick = { navController.navigate(Screen.Notification.route) },
                onHistorialClick = { /* aquí podrías navegar a Historial */ },
                onGuardadoClick = { /* aquí podrías navegar a Guardado */ }
            )
        }

        composable(Screen.Notification.route) {
            val notificationViewModel: NotificationViewModel = hiltViewModel()
            NotificationScreen(
                modifier = modifier,
                viewModel = notificationViewModel
            )
        }

        composable(Screen.SettingsRoute.route) {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            SettingsScreen(
                viewModel = settingsViewModel,
                onLogoutClick = {
                    navController.navigate(Screen.StartRoute.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "detail/{gastroBarId}",
            arguments = listOf(navArgument("gastroBarId") { type = NavType.IntType })
        ) {
            val detailViewModel: DetailGastroBarViewModel = hiltViewModel()
            val gastroBarId = it.arguments?.getInt("gastroBarId") ?: 0
                DetailGastroBarScreen(
                    modifier = modifier,
                    viewModel = detailViewModel,
                    gastroBarId = gastroBarId
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
    BottomNavItem(Icons.Filled.Search, Icons.Outlined.Search, Screen.Search.route),
    BottomNavItem(Icons.Filled.AddCircle, Icons.Outlined.AddCircle, Screen.Create.route),
    BottomNavItem(Icons.Filled.Notifications, Icons.Outlined.Notifications, Screen.Events.route),
    BottomNavItem(Icons.Filled.Person, Icons.Outlined.Person, Screen.Profile.route)
)
