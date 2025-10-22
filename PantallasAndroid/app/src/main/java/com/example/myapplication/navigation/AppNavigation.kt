package com.example.myapplication.navigation

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myapplication.data.local.LocalGastroBarProvider
import com.example.myapplication.ui.Splash.SplashScreen
import com.example.myapplication.ui.barReviews.BarReviewsScreen
import com.example.myapplication.ui.barReviews.BarReviewsViewModel
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
import com.example.myapplication.ui.profile.EditProfileScreen
import com.example.myapplication.ui.profile.EditProfileViewModel
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
import com.example.myapplication.ui.user.UserScreen
import com.example.myapplication.ui.user.UserViewModel

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
    object User : Screen("user/{userId}") {
        fun createRoute(userId: String) = "user/$userId"
    }

    object Profile : Screen("profile")
    object EditProfile : Screen("editProfile")
    object SettingsRoute : Screen("settings")
    object Notification : Screen("notification")
    // BarReviews already encoded to accept strings — keep it that way
    object BarReviews : Screen("barReviews/{gastroBarId}?gastroBarName={gastroBarName}") {
        fun createRoute(gastroBarId: String, gastroBarName: String? = null) =
            "barReviews/$gastroBarId?gastroBarName=${gastroBarName ?: ""}"
    }
    object Splash : Screen("splash")
}

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
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
            val uiState by homeViewModel.uiState.collectAsState()

            HomeScreen(
                modifier = modifier,
                viewModel = homeViewModel,
                // now pass reviewId as String
                onReviewClick = { reviewIdStr ->
                    // find by string id
                    val review = uiState.reviews.find { it.id == reviewIdStr }
                    val gastroBar = review?.placeName?.let { placeName ->
                        LocalGastroBarProvider.gastroBars.find { it.name == placeName }
                    }
                    Log.d("HomeScreen", "Review ID (string): $reviewIdStr")
                    Log.d("HomeScreen", "GastroBar encontrado: ${gastroBar?.id}")
                    gastroBar?.let {
                        navController.navigate("detail/${it.id}")
                    }
                },
                onUserClick = { userId ->
                    Log.d("HomeScreen", "Usuario clicado con ID: $userId")
                    navController.navigate(Screen.User.createRoute(userId))
                }
            )
        }

        composable(Screen.Splash.route) {
            SplashScreen(
                navigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(id = 0) { inclusive = true }
                    }
                },
                navigateToStart = {
                    navController.navigate(Screen.StartRoute.route) {
                        popUpTo(id = 0) { inclusive = true }
                    }
                },
                splashViewModel = hiltViewModel()
            )
        }

        composable(Screen.Search.route) {
            val searchViewModel: SearchViewModel = hiltViewModel()
            SearchScreen(
                gastroBars = LocalGastroBarProvider.gastroBars,
                onGastroBarClick = { gastroBarId ->
                    // gastroBarId is string already
                    navController.navigate("detail/$gastroBarId")
                },
                viewModel = searchViewModel
            )
        }

        composable(Screen.Create.route) {
            val createViewModel: CreateViewModel = hiltViewModel()
            CreateScreen(
                viewModel = createViewModel,
                onSaveClick = {
                    createViewModel.createReview()
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Events.route) {
            val eventViewModel: EventViewModel = hiltViewModel()
            EventScreen(
                onEventClick = { /* ... */ },
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
                onHistorialClick = { },
                onGuardadoClick = { },
                onEditProfileClick = { navController.navigate(Screen.EditProfile.route) }
            )
        }

        composable(Screen.EditProfile.route) {
            val editProfileViewModel: EditProfileViewModel = hiltViewModel()
            EditProfileScreen(
                modifier = modifier,
                viewModel = editProfileViewModel
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

        // DETAIL (gastroBarId is String)
        composable(
            route = "detail/{gastroBarId}",
            arguments = listOf(navArgument("gastroBarId") { type = NavType.StringType })
        ) { backStackEntry ->
            val gastroBarIdStr = backStackEntry.arguments?.getString("gastroBarId") ?: ""
            Log.d("NavDebug", "Navigating to detail with gastroBarId='$gastroBarIdStr'")

            val detailViewModel: DetailGastroBarViewModel = hiltViewModel()
            DetailGastroBarScreen(
                viewModel = detailViewModel,
                gastroBarId = gastroBarIdStr,
                // pass Strings forward
                onViewReviewsClick = { idStr, name ->
                    Log.d("NavDebug", "onViewReviewsClick -> id='$idStr', name='$name'")
                    navController.navigate(Screen.BarReviews.createRoute(idStr, name))
                }
            )
        }

        // BAR REVIEWS (accept gastroBarId as String)
        composable(
            route = Screen.BarReviews.route,
            arguments = listOf(
                navArgument("gastroBarId") { type = NavType.StringType },
                navArgument("gastroBarName") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val barReviewsVM: BarReviewsViewModel = hiltViewModel(backStackEntry)
            val gastroBarIdStr = backStackEntry.arguments?.getString("gastroBarId") ?: ""
            val gastroBarName = backStackEntry.arguments?.getString("gastroBarName") ?: ""

            val state by barReviewsVM.uiState.collectAsState()

            // Pass gastroBarIdStr as String to screen/viewmodel (update BarReviewsScreen signature if needed)
            BarReviewsScreen(
                gastroBarId = gastroBarIdStr,
                gastroBarName = gastroBarName,
                onReviewClick = { /* ... */ },
                onUserClick = { userIdStr ->
                    navController.navigate(Screen.User.createRoute(userIdStr))
                }
            )
        }

        composable(
            route = "user/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val userViewModel: UserViewModel = hiltViewModel(backStackEntry)

            LaunchedEffect(userId) {
                if (userId.isNotBlank()) {
                    userViewModel.loadUser(userId)
                }
            }

            UserScreen(viewModel = userViewModel)
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
