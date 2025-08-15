package com.example.myapplication.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier
) {
    // Ejemplo de datos para la pantalla
    val userName = "Ashley"
    val userEmail = "ashley.smith@example.com"
    val profilePic = R.drawable.usr1

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Encabezado de la pantalla
        ProfileHeader(
            userImage = profilePic,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Detalles del perfil y botón de edición
        ProfileDetails(
            userName = userName,
            userEmail = userEmail,
            onEditProfileClick = { /* */ }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Menú de opciones
        ProfileMenu()

        Spacer(modifier = Modifier.weight(1f))

        // Botón de cerrar sesión
        LogoutButton(
            onLogoutClick = { /* */ }
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
            Image(
                painter = painterResource(id = R.drawable.logo), // Placeholder
                contentDescription = "NOCTA Logo",
                modifier = Modifier.size(100.dp)
            )
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
            .offset(y = (-40).dp), // Subimos este composable para que se superponga
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
fun ProfileMenu(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(horizontal = 24.dp)) {
        ProfileMenuItem(
            icon = Icons.Default.History,
            text = "History",
            onClick = { /* ... */ }
        )
        ProfileMenuItem(
            icon = Icons.Default.Bookmark,
            text = "Saved",
            onClick = { /* ... */ }
        )
        ProfileMenuItem(
            icon = Icons.Default.Notifications,
            text = "Notifications",
            onClick = { /* ... */ }
        )
        ProfileMenuItem(
            icon = Icons.Default.Settings,
            text = "Settings",
            onClick = { /* ... */ }
        )
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onClick) {
            Icon(imageVector = icon, contentDescription = text)
        }
        TextButton(onClick = onClick, modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = text, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.weight(1f))
                Icon(imageVector = Icons.Default.ArrowForwardIos, contentDescription = "Arrow", modifier = Modifier.size(16.dp))
            }
        }
    }
    Divider(color = Color.LightGray)
}

@Composable
fun LogoutButton(
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onLogoutClick,
        modifier = modifier.padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Log out",
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = "Logout",
                tint = Color.Red
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        ProfileScreen()
    }
}