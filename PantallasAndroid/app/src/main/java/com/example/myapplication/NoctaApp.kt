package com.example.myapplication

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.navigation.AppNavigation
import com.example.myapplication.navigation.NavigationLogic
import com.example.myapplication.navigation.Screen
import com.example.myapplication.ui.user.UserViewModel
import com.example.myapplication.utils.NoctaBottomNavigationBar
import com.example.myapplication.utils.NoctaTopBar

import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun NoctaApp(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route

   // val userId: String? = currentUser?.uid ?: ""
    val showBar = currentRoute != Screen.StartRoute.route &&
            currentRoute != Screen.Login.route &&
            currentRoute != Screen.Register.route

    Scaffold(
        modifier = modifier,
        topBar = {
            if (NavigationLogic.shouldShowTopBar(currentRoute)) {
                NoctaTopBar()
            }
        },
        bottomBar = {
            if (NavigationLogic.shouldShowBottomBar(currentRoute)) {
                NoctaBottomNavigationBar(
                    navController = navController
                )
            }
        }
    ) {
        AppNavigation(
            modifier = Modifier.padding(
                bottom = it.calculateBottomPadding(),
                // top = it.calculateTopPadding()
            ),
            navController = navController
        )
    }
}

