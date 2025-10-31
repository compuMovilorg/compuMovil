package com.example.myapplication

import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.navigation.AppNavigation
import com.example.myapplication.navigation.NavigationLogic
import com.example.myapplication.navigation.Screen
import com.example.myapplication.ui.user.UserViewModel
import com.example.myapplication.utils.NoctaBottomNavigationBar
import com.example.myapplication.utils.NoctaTopBar
import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.ui.platform.LocalContext

import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.messaging.FirebaseMessaging

@Composable
fun NoctaApp(
    modifier: Modifier = Modifier
) {

    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
        if (!task.isSuccessful) {
            Log.d("Token",task.result)
        } else {
            Log.d("Token","Error al obtener token")
        }
    }
    val navController = rememberNavController()
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route

    val context = LocalContext.current


    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                Log.d("NoctaApp", "Permiso de notificación otorgado")
            } else {
                Log.d("NoctaApp", "Permiso de notificación denegado")

            }
        }
    )

//    LaunchedEffect(Unit) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            if (ContextCompat.checkSelfPermission(
//                    context,
//                    Manifest.permission.POST_NOTIFICATIONS
//                ) != PackageManager.PERMISSION_GRANTED) {
//                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
//            }
//        }
//    }

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

