package com.example.myapplication.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.utils.ProfileAsyncImage
import com.example.myapplication.utils.SettingsOption

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    onConfiguracionClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onHistorialClick: () -> Unit,
    onGuardadoClick: () -> Unit,
    onEditProfileClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier.fillMaxSize().testTag("profile_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileHeader(
            userImageUrl = state.profilePicUrl,
            modifier = Modifier.padding(top = 20.dp)
        )

        Spacer(modifier = Modifier.padding(bottom = 8.dp))

        ProfileDetails(
            userName = state.userName,
            userEmail = state.userEmail,
            onEditProfileClick = onEditProfileClick
        )

        Spacer(modifier = Modifier.height(8.dp))

        ProfileMenu(
            onHistorialClick = onHistorialClick,
            onGuardadoClick = onGuardadoClick,
            onNotificationClick = onNotificationClick,
            onConfiguracionClick = onConfiguracionClick
        )
    }
}

@Composable
fun ProfileHeader(
    userImageUrl: String?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.height(250.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(32.dp))
            ProfileAsyncImage(
                profileImage = userImageUrl ?: "",
                size = 100
            )
        }
    }
}

@Composable
fun ProfileDetails(
    userName: String,
    userEmail: String,
    onEditProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .offset(y = (-40).dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = userName,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = userEmail,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onEditProfileClick,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = CircleShape,
            modifier = Modifier.testTag("btn_edit_profile")
        ) {
            Text(text = "Edit Profile")
        }
    }
}

@Composable
fun ProfileMenu(
    modifier: Modifier = Modifier,
    onHistorialClick: () -> Unit,
    onGuardadoClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onConfiguracionClick: () -> Unit
) {
    Column(modifier = modifier.padding(horizontal = 24.dp)) {
        SettingsOption(
            icon = { Icon(Icons.Default.History, contentDescription = "Historial") },
            title = "Historial",
            onClick = onHistorialClick
        )
        SettingsOption(
            icon = { Icon(Icons.Default.Bookmark, contentDescription = "Guardado") },
            title = "Guardado",
            onClick = onGuardadoClick
        )
        SettingsOption(
            icon = { Icon(Icons.Default.Notifications, contentDescription = "Notificaciones") },
            title = "Notificaciones",
            onClick = onNotificationClick
        )
        SettingsOption(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Configuración") },
            title = "Configuración",
            onClick = onConfiguracionClick
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        ProfileScreen(
            modifier = Modifier.fillMaxSize(),
            onConfiguracionClick = {},
            onNotificationClick = {},
            onHistorialClick = {},
            onGuardadoClick = {},
            onEditProfileClick = {}
        )
    }
}
