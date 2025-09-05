package com.example.myapplication.ui.profile

import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.R
import com.example.myapplication.utils.SettingsOption
import androidx.compose.runtime.LaunchedEffect

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel(),
    onEditProfile: () -> Unit,
    onHistory: () -> Unit,
    onSaved: () -> Unit,
    onNotifications: () -> Unit,
    onSettings: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                ProfileEvent.EditProfile -> onEditProfile()
                ProfileEvent.OpenHistory -> onHistory()
                ProfileEvent.OpenSaved -> onSaved()
                ProfileEvent.OpenNotifications -> onNotifications()
                ProfileEvent.OpenSettings -> onSettings()
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Encabezado de la pantalla
        ProfileHeader(
            userImage = state.profilePic,
            modifier = Modifier.padding(top = 20.dp)
        )

        Spacer(modifier = Modifier.padding(bottom = 8.dp))

        ProfileDetails(
            userName = state.userName,
            userEmail = state.userEmail,
            onEditProfileClick = { viewModel.editProfile() }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Menú de opciones
        ProfileMenu(
            onHistoryClick = { viewModel.openHistory() },
            onSavedClick = { viewModel.openSaved() },
            onNotificationClick = { viewModel.openNotifications() },
            onSettingsClick = { viewModel.openSettings() }

        )
    }
}


@Composable
fun ProfileHeader(
    userImage: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(250.dp),
        contentAlignment = Alignment.Center
    ) {

        // Contenido superpuesto: logo y foto de perfil
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(32.dp))
            Image(
                painter = painterResource(id = userImage),
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
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
        // Nombre de usuario
        Text(
            text = userName,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
        // Email de usuario
        Text(
            text = userEmail,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Botón para editar perfil
        Button(
            onClick = onEditProfileClick,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = CircleShape
        ) {
            Text(text = "Edit Profile")
        }
    }
}

@Composable
fun ProfileMenu(
    modifier: Modifier = Modifier,
    onHistoryClick: () -> Unit,
    onSavedClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Column(modifier = modifier.padding(horizontal = 24.dp)) {
        SettingsOption(
            icon = { Icon(Icons.Default.History, contentDescription = "Historial") },
            title = "Historial",
            onClick = onHistoryClick
        )
        SettingsOption(
            icon = { Icon(Icons.Default.Bookmark, contentDescription = "Guardado") },
            title = "Guardado",
            onClick = onSavedClick
        )
        SettingsOption(
            icon = { Icon(Icons.Default.Notifications, contentDescription = "Notificaciones") },
            title = "Notificaciones",
            onClick = onNotificationClick
        )
        SettingsOption(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Configuración") },
            title = "Configuración",
            onClick = onSettingsClick
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        ProfileScreen(
            onEditProfile = {},
            onHistory = {},
            onSaved = {},
            onNotifications = {},
            onSettings = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}