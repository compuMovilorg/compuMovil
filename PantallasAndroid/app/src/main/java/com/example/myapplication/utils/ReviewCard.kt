package com.example.myapplication.utils

import android.util.Log
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myapplication.data.ReviewInfo

@Composable
fun ReviewCard(
    review: ReviewInfo,
    onReviewClick: (String) -> Unit,
    onUserClick: (String) -> Unit,
    onLikeClick: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val reviewId = review.id.ifBlank { "unknown" }
    val userId = review.userId.ifBlank { "unknown_user" }

    Card(
        modifier = modifier
            .fillMaxWidth()
            // contenedor raíz de la card
            .testTag("review_card_$reviewId")
            // tag específico para click en el review (lo mantengo)
            .testTag("review_item_$reviewId")
            .clickable {
                if (review.id.isNotBlank()) onReviewClick(review.id)
                else android.util.Log.w("ReviewCard", "Review id vacío o nulo, click ignorado")
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
                // tag al contenedor clickeable del header (ya lo tenías)
                modifier = Modifier.testTag("review_user_$userId"),
                onUserClick = {
                    if (review.userId.isNotBlank()) onUserClick(review.userId)
                    else android.util.Log.w("ReviewCard", "User id vacío o nulo, click ignorado")
                }
            )

            // Nombre del lugar
            Text(
                text = review.placeName,
                modifier = Modifier.testTag("review_place_name_$reviewId"),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )

            // Imagen del lugar
            coil.compose.AsyncImage(
                model = review.placeImage ?: "",
                contentDescription = "Imagen del lugar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .testTag("review_image_$reviewId")
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(MaterialTheme.shapes.medium)
            )

            // Texto de la reseña
            ReviewCardBody(
                reviewText = review.reviewText,
                modifier = Modifier.testTag("review_text_$reviewId")
            )

            // Footer
            ReviewCardFooter(
                reviewId = reviewId,
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
    modifier: Modifier = Modifier,
    onUserClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
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
            // nombre visible del usuario con tag propio
            modifier = Modifier.testTag("review_user_name_${(userName ?: "").ifBlank { "unknown" }}"),
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
        )
    }
}

@Composable
fun ReviewCardBody(
    reviewText: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = reviewText,
        modifier = modifier,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
fun ReviewCardFooter(
    reviewId: String,
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
            IconButton(
                onClick = onLikeClick,
                modifier = Modifier.testTag("review_like_btn_$reviewId")
            ) { Icon(Icons.Default.Favorite, contentDescription = "Me gusta") }

            Text(
                text = "$likes",
                modifier = Modifier.testTag("review_like_count_$reviewId")
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onCommentClick,
                modifier = Modifier.testTag("review_comment_btn_$reviewId")
            ) { Icon(Icons.Default.Comment, contentDescription = "Comentar") }

            Text(
                text = "$comments",
                modifier = Modifier.testTag("review_comment_count_$reviewId")
            )
        }

        IconButton(
            onClick = onShareClick,
            modifier = Modifier.testTag("review_share_btn_$reviewId")
        ) { Icon(Icons.Default.Share, contentDescription = "Compartir") }
    }
}

