package com.example.myapplication.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myapplication.data.ReviewInfo
import android.util.Log

@Composable
fun ReviewCard(
    review: ReviewInfo,
    onReviewClick: (String) -> Unit,
    onUserClick: (String) -> Unit,
    onLikeClick: (String, String) -> Unit, // reviewId, userId
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                if (review.id.isNotBlank()) {
                    onReviewClick(review.id)
                } else {
                    Log.w("ReviewCard", "Review id vacío o nulo, click ignorado")
                }
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header: perfil
            ReviewCardHeader(
                userImage = review.userImage,
                userName = review.name,
                onUserClick = {
                    if (review.userId.isNotBlank()) {
                        onUserClick(review.userId)
                    } else {
                        Log.w("ReviewCard", "User id vacío o nulo, click ignorado")
                    }
                }
            )

            // Nombre del lugar
            Text(
                text = review.placeName,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )

            // Imagen del lugar
            AsyncImage(
                model = review.placeImage ?: "",
                contentDescription = "Imagen del lugar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(MaterialTheme.shapes.medium)
            )

            // Texto de la reseña
            ReviewCardBody(review.reviewText)

            // Footer
            ReviewCardFooter(
                likes = review.likes,
                comments = review.comments,
                onLikeClick = { onLikeClick(review.id, review.userId) },
                onCommentClick = { /* TODO: acción comentar */ },
                onShareClick = { /* TODO: acción compartir */ }
            )
        }
    }
}

@Composable
fun ReviewCardHeader(
    userImage: String?,
    userName: String?,
    onUserClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onUserClick() }
            .padding(vertical = 4.dp)
    ) {
        ProfileAsyncImage(
            profileImage = userImage ?: "",
            size = 60
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = userName ?: "",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
        )
    }
}

@Composable
fun ReviewCardBody(reviewText: String) {
    Text(
        text = reviewText,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
fun ReviewCardFooter(
    likes: Int,
    comments: Int,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onShareClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onLikeClick) {
                Icon(Icons.Default.Favorite, contentDescription = "Me gusta")
            }
            Text(text = "$likes")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onCommentClick) {
                Icon(Icons.Default.Comment, contentDescription = "Comentar")
            }
            Text(text = "$comments")
        }

        IconButton(onClick = onShareClick) {
            Icon(Icons.Default.Share, contentDescription = "Compartir")
        }
    }
}
