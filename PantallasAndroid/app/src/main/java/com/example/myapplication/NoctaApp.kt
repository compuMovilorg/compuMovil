package com.example.myapplication

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.navigation.AppNavigation
import com.example.myapplication.navigation.NavigationLogic
import com.example.myapplication.navigation.Screen
import com.example.myapplication.utils.NoctaBottomNavigationBar

@Composable
fun NoctaApp(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route

    val showBar = currentRoute != Screen.Start.route && currentRoute != Screen.Login.route && currentRoute != Screen.Register.route

    Scaffold(
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

//        RegisterScreen(modifier = Modifier.padding(innerPadding))
//         HomeScreen(modifier = Modifier.padding(innerPadding))
        //  LoginScreen(modifier = Modifier.padding(innerPadding))
        // ResetPasswordScreen(modifier = Modifier.padding(innerPadding))
        // ReviewScreen(modifier = Modifier.padding(innerPadding))
        // ProfileScreen(modifier = Modifier.padding(innerPadding))
        // DetailGastroBarScreen(modifier = Modifier.padding(innerPadding))
//    }
//}
