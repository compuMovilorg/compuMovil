package com.example.myapplication.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.auth.CurrentUserProvider
import com.example.myapplication.data.repository.ReviewRepository
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.utils.ProfileAsyncImage
import com.example.myapplication.utils.SettingsOption
import com.example.myapplication.utils.ReviewCard
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.example.myapplication.utils.ReviewCard
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    onConfiguracionClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onHistorialClick: (Int) -> Unit,
    onGuardadoClick: () -> Unit,
    onEditProfileClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            ProfileHeader(
                userImageUrl = state.profilePicUrl,
                modifier = Modifier.padding(top = 20.dp)
            )
        }


        item { Spacer(modifier = Modifier.height(8.dp)) }

        item {
            ProfileDetails(
                userName = state.userName,
                userEmail = state.userEmail,
                onEditProfileClick = onEditProfileClick
            )
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        item {
            ProfileMenu(
                modifier = Modifier.fillMaxWidth(),
                onHistorialClick = {
                    state.userId?.let(onHistorialClick)
                },
                onGuardadoClick = onGuardadoClick,
                onNotificationClick = onNotificationClick,
                onConfiguracionClick = onConfiguracionClick
            )
        }

        item {
            Divider(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp))
        }

        item {
            ProfileReviewsHeader()
        }

        when {
            state.isLoadingReviews -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            state.reviewsError != null -> {
                item {
                    Text(
                        text = state.reviewsError ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    )
                }
            }

            state.reviews.isEmpty() -> {
                item {
                    Text(
                        text = "Aún no has publicado reseñas.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    )
                }
            }

            else -> {
                items(state.reviews, key = { it.id }) { review ->
                    ReviewCard(
                        onReviewClick = {},
                        onUserClick = {},
                        review = review,
                        modifier = Modifier
                            .padding(horizontal = 24.dp, vertical = 8.dp)
                    )
                }
            }
        }
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
            shape = CircleShape
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
            onHistorialClick = { _ -> },
            onGuardadoClick = {},
            onEditProfileClick = {}
        )
    }
}

@Composable
fun ProfileReviewsHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Historial de reseñas",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun ProfileReviewsError(
    message: String,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Button(
                onClick = onRetryClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onErrorContainer,
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(text = "Reintentar")
            }
        }
    }
}