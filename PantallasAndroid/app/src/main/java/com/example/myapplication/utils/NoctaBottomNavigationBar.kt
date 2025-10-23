package com.example.myapplication.utils

import android.util.Log
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
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
            // ruta actual (puede ser "detail/123", "user/abc", etc.)
            val currentDestination = navController.currentDestination?.route

            // selected robusto: igualdad exacta o prefijo para rutas con argumentos
            val selected = when {
                currentDestination == null -> false
                currentDestination == item.route -> true
                // Si el item representa la pantalla User (con path "user/{userId}")
                // marcamos seleccionado cuando la ruta actual empiece por "user/"
                item.route == Screen.User.route && currentDestination.startsWith("user/") -> true
                else -> false
            }

            NavigationBarItem(
                selected = selected,
                onClick = {
                    try {
                        // Si en el bottomNavItems tienes Screen.User.route y quieres que
                        // el icono lleve al mainUser (tu caso actual), navegamos a MainUser:
                        val routeToNavigate = when (item.route) {
                            Screen.User.route -> {
                                // Opción A: ir a MainUser (si quieres que el icono "persona"
                                // abra la pantalla del usuario logueado)
                                Screen.MainUser.route

                                // Opción B (alternativa): ir a la ruta "user/{id}" del usuario actual:
                                // val currentUserId = ... obtener id desde tu CurrentUserProvider
                                // Screen.User.createRoute(currentUserId)
                            }
                            else -> item.route
                        }

                        Log.d("NavDebug", "BottomBar -> navigating to '$routeToNavigate' (item.route='${item.route}')")
                        navController.navigate(routeToNavigate) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    } catch (e: Exception) {
                        // Loguear la excepción para que no crashee la app y podamos diagnosticar
                        Log.e("NavDebug", "Error navegando desde BottomBar al route='${item.route}'", e)
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
