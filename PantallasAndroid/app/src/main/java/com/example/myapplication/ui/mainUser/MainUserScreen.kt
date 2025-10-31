package com.example.myapplication.ui.mainuser

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.data.ReviewInfo
import com.example.myapplication.utils.EditReviewDialog
import com.example.myapplication.utils.MyReviewItem
import com.example.myapplication.utils.ProfileAsyncImage

@Composable
fun MainUserScreen(
    viewModel: MainUserViewModel = hiltViewModel(),
    onNavigateToProfile: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    MainUserBody(
        state = state,
        onRetry = { viewModel.load() },
        onEdit = { viewModel.startEdit(it) },
        onDelete = { viewModel.deleteReview(it) },
        onEditTextChange = { viewModel.updateEditingText(it) },
        onCancelEdit = { viewModel.cancelEdit() },
        onConfirmEdit = { viewModel.confirmEdit() },
        onNavigateToProfile = onNavigateToProfile,
        modifier = Modifier.testTag("main_user_screen")
    )
}

@Composable
private fun MainUserBody(
    state: MainUserState,
    onRetry: () -> Unit,
    onEdit: (ReviewInfo) -> Unit,
    onDelete: (String) -> Unit,
    onEditTextChange: (String) -> Unit,
    onCancelEdit: () -> Unit,
    onConfirmEdit: () -> Unit,
    onNavigateToProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        state.isLoading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        state.errorMessage != null && state.user == null -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = state.errorMessage)
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = onRetry) { Text("Reintentar") }
                }
            }
        }

        state.user != null -> {
            val user = state.user
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 100.dp)
                    ) {
                        ProfileAsyncImage(
                            profileImage = user?.profileImage ?: "",
                            size = 90
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier.weight(1f)
                        ) {
                            // 🔹 Nombre de usuario con icono de configuración
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = user?.username.orEmpty(),
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                IconButton(
                                    onClick = onNavigateToProfile,
                                    modifier = Modifier.size(24.dp).testTag("btn_profile_settings")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = "Configuración",
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }

                            val displayName = user?.name ?: ""
                            if (displayName.isNotBlank()) {
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(text = displayName, style = MaterialTheme.typography.bodyMedium)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Text(
                                    text = "Seguidores: ${user?.followersCount ?: 0}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text("·", style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    text = "Siguiendo: ${user?.followingCount ?: 0}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (state.reviews.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Aún no has publicado reseñas")
                        }
                    }
                } else {
                    items(
                        items = state.reviews,
                        key = { it.id }
                    ) { review ->
                        MyReviewItem(
                            review = review,
                            isDeleting = state.deletingId == review.id,
                            onEdit = { onEdit(review) },
                            onDelete = { onDelete(review.id) }
                        )
                    }
                }

                if (state.errorMessage != null) {
                    item {
                        Spacer(Modifier.height(8.dp))
                        AssistChip(
                            onClick = onRetry,
                            label = { Text(state.errorMessage) }
                        )
                    }
                }
            }

            if (state.editing != null) {
                EditReviewDialog(
                    editing = state.editing,
                    onTextChange = onEditTextChange,
                    onCancel = onCancelEdit,
                    onConfirm = onConfirmEdit
                )
            }
        }

        else -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay datos para mostrar")
            }
        }
    }
}
