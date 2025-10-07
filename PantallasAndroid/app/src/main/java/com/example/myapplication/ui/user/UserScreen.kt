package com.example.myapplication.ui.user

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

// -----------------------------------------------------------------------------
// PANTALLA PRINCIPAL
// -----------------------------------------------------------------------------
@Composable
fun UserScreen(
    viewModel: UserViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    UserScreenBody(
        state = state,
        onRetry = { viewModel.reload() },
        modifier = Modifier
    )
}

// -----------------------------------------------------------------------------
// BODY PRINCIPAL CON TODA LA UI
// -----------------------------------------------------------------------------
@Composable
fun UserScreenBody(
    state: UserState,
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
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // -----------------------------------------------------------------
                // ENCABEZADO DEL PERFIL (imagen izquierda, nombre/username y contadores en fila)
                // -----------------------------------------------------------------
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 100.dp) // deja tu offset si así lo quieres
                    ) {
                        // Imagen de perfil
                        ProfileAsyncImage(
                            profileImage = user.profileImage ?: "",
                            size = 90
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        // Información del usuario
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier.weight(1f)
                        ) {
                            // Username (grande y en negrita)
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

                            Spacer(modifier = Modifier.height(8.dp))

                            // Contadores en una sola fila
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

                // -----------------------------------------------------------------
                // LISTA DE REVIEWS DEL USUARIO
                // -----------------------------------------------------------------
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
                        key = { it.id } // clave estable para mejor rendimiento
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

// -----------------------------------------------------------------------------
// ITEM INDIVIDUAL DE RESEÑA
// -----------------------------------------------------------------------------
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