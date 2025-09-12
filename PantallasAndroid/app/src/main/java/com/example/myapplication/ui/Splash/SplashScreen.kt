package com.example.myapplication.ui.Splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun SplashScreen(
    navigateToHome: () -> Unit,
    navigateToStart: () -> Unit,
    splashViewModel: SplashViewModel
) {
    val navigateHome by splashViewModel.navigateHome.collectAsState()

    LaunchedEffect(navigateHome) {
        if (navigateHome) {
            navigateToHome()
        } else {
            navigateToStart()
        }
    }
}
