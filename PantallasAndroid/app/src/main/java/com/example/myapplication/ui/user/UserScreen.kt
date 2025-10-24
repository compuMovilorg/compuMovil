package com.example.myapplication.ui.user

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.data.ReviewInfo
import com.example.myapplication.utils.ProfileAsyncImage

@Composable
fun UserScreen(
    viewModel: UserViewModel = hiltViewModel()
) {
    Log.d("UserScreen", "UserScreen Composable inicializado")
    val state by viewModel.uiState.collectAsState()

    UserScreenBody(
        state = state,
        onRetry = { viewModel.reload() },
        modifier = Modifier,
        viewModel = viewModel
    )
}

@Composable
fun UserScreenBody(
    state: UserState,
    viewModel: UserViewModel,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    when {
        state.isLoading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        state.errorMessage != null -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = state.errorMessage!!)
                    Spacer(Modifier.height(12.dp))
                    if (onRetry != null) {
                        Button(onClick = onRetry) { Text("Reintentar") }
                    }
                }
            }
        }

        state.user != null -> {
            val user = state.user!!
            Log.d("UserScreen", "Renderizando perfil de usuario: ${user.username}")

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
                            profileImage = user.profileImage ?: "",
                            size = 90
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column {
                                    Text(
                                        text = user.username,
                                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                                    )

                                    val displayName = user.name ?: ""
                                    if (displayName.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = displayName,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }

                                // Botón "Seguir"
                                Button(
                                    onClick = {
                                        Log.d("UserScreen", "CLICK detectado en boton Seguir para userId=${user.id}")
                                        if (!user.id.isNullOrBlank()) {
                                            viewModel.followOrUnfollowUser(user.id)
                                        } else {
                                            Log.e("UserScreen", "user.id está vacío, no se puede seguir")
                                        }
                                    },
                                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                                    modifier = Modifier.height(36.dp)
                                ) {
                                    Text("Seguir", style = MaterialTheme.typography.bodyMedium)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Text(
                                    text = "Seguidores: ${user.followersCount ?: 0}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text("·", style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    text = "Siguiendo: ${user.followingCount ?: 0}",
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
                            Text(text = "Este usuario no tiene publicaciones aún")
                        }
                    }
                } else {
                    items(
                        items = state.reviews,
                        key = { it.id }
                    ) { review ->
                        ReviewItem(review)
                    }
                }
            }
        }

        else -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay datos para mostrar")
            }
        }
    }
}

@Composable
fun ReviewItem(review: ReviewInfo) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        if (!review.placeImage.isNullOrEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(review.placeImage),
                contentDescription = "Imagen del review",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Text(
            text = review.placeName,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = review.reviewText,
            style = MaterialTheme.typography.bodyMedium
        )

        Divider(modifier = Modifier.padding(top = 8.dp))
    }
}
