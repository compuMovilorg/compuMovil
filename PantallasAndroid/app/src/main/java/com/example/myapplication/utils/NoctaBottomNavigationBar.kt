package com.example.myapplication.utils

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.BottomAppBarDefaults.windowInsets
import androidx.compose.material3.BottomSheetDefaults.windowInsets
import androidx.compose.material3.DrawerDefaults.windowInsets
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarDefaults.windowInsets
import androidx.compose.material3.NavigationBarItem

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.navigation.bottomNavItems

@Composable
fun NoctaBottomNavigationBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.height(56.dp),
        windowInsets = WindowInsets.safeDrawing.only(
            WindowInsetsSides.Top + WindowInsetsSides.Start + WindowInsetsSides.End
        )
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
