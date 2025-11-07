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
import androidx.compose.ui.platform.testTag
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
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .testTag("profile_screen_loading"),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        state.errorMessage != null -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .testTag("profile_screen_error"),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = state.errorMessage!!)
                    Spacer(Modifier.height(12.dp))
                    if (onRetry != null) {
                        Button(onClick = onRetry, modifier = Modifier.testTag("btn_profile_retry")) {
                            Text("Reintentar")
                        }
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
                    .testTag("profile_screen")
            ) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 100.dp)
                            .testTag("profile_header")
                    ) {
                        ProfileAsyncImage(
                            profileImage = user.profileImage ?: "",
                            size = 90
                        )
                        // Si quieres tag para el avatar: envuelve ProfileAsyncImage con .testTag("profile_avatar")

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
                                Column(modifier = Modifier.testTag("profile_identity")) {
                                    // Username (lo muestras como username “crudo”, no con @)
                                    Text(
                                        text = user.username,
                                        modifier = Modifier.testTag("profile_username"),
                                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                                    )

                                    val displayName = user.name ?: ""
                                    if (displayName.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = displayName,
                                            modifier = Modifier.testTag("profile_name"),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }

                                // Botón Seguir / Siguiendo (reactivo al estado 'followed')
                                val isFollowing = user.followed == true
                                Button(
                                    onClick = {
                                        Log.d("UserScreen", "CLICK boton seguir/siguiendo para userId=${user.id}")
                                        if (!user.id.isNullOrBlank()) {
                                            viewModel.followOrUnfollowUser(user.id)
                                        } else {
                                            Log.e("UserScreen", "user.id vacío, no se puede seguir")
                                        }
                                    },
                                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                                    modifier = Modifier
                                        .height(36.dp)
                                        .testTag("btn_follow")
                                ) {
                                    Text(
                                        text = if (isFollowing) "Siguiendo" else "Seguir",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.testTag("profile_counters")
                            ) {
                                Text(
                                    text = "Seguidores: ${user.followersCount ?: 0}",
                                    modifier = Modifier.testTag("followers_count"),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text("·", style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    text = "Siguiendo: ${user.followingCount ?: 0}",
                                    modifier = Modifier.testTag("following_count"),
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
                                .padding(20.dp)
                                .testTag("profile_reviews_empty"),
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
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .testTag("profile_screen_empty"),
                contentAlignment = Alignment.Center
            ) {
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
            .testTag("profile_review_item_${review.id}")
    ) {
        if (!review.placeImage.isNullOrEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(review.placeImage),
                contentDescription = "Imagen del review",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .testTag("profile_review_image_${review.id}"),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Text(
            text = review.placeName,
            modifier = Modifier.testTag("profile_review_place_${review.id}"),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = review.reviewText,
            modifier = Modifier.testTag("profile_review_text_${review.id}"),
            style = MaterialTheme.typography.bodyMedium
        )

        Divider(modifier = Modifier.padding(top = 8.dp))
    }
}
