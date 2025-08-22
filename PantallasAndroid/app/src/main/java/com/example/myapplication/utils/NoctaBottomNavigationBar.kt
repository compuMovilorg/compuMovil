package com.example.myapplication.utils

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.isSelected
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.navigation.bottomNavItems

@Composable
fun NoctaBottomNavigationBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
    ) {
        bottomNavItems.forEach { item ->
            val isSelected = item.route == navController.currentDestination?.route
            val currentRoute = navController.currentDestination?.route
            NavigationBarItem(
                icon =
                    {
                    Icon(
                        imageVector = if(isSelected) item.filledIcon else item.outlinedIcon,
                        contentDescription = item.route
                    )
                },
                selected = false,
                onClick = {
                    navController.navigate(item.route) {
                    }
                }
            )
        }
    }
}
@Preview
@Composable
fun NoctaBottomNavigationBarPreview() {
    NoctaBottomNavigationBar(
        navController = rememberNavController()
    )
}
