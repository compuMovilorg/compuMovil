package com.example.myapplication.utils

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.navigation.Screen
import com.example.myapplication.navigation.bottomNavItems

@Composable
fun NoctaBottomNavigationBar(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavigationBar(
        modifier = modifier.height(64.dp)
    ) {
        bottomNavItems.forEach { item ->
            val currentDestination = navController.currentDestination?.route
            val selected = currentDestination == item.route ||
                    (currentDestination?.startsWith("user/") == true && item.route == Screen.User.route)

            NavigationBarItem(
                selected = selected,
                onClick = {
                    // 👇 Si el ítem es el perfil, solo navega a "user" base (sin id)
                    val route = if (item.route == Screen.User.route) {
                        Screen.User.route   // → "user"
                    } else {
                        item.route
                    }

                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    val icon = if (selected) item.filledIcon else item.outlinedIcon
                    Icon(
                        imageVector = icon,
                        contentDescription = item.route
                    )
                },
                alwaysShowLabel = false,
                label = null
            )
        }
    }
}
