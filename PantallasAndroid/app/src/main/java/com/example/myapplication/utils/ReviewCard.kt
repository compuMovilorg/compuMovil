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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myapplication.data.ReviewInfo

@Composable
fun ReviewCard(
    onReviewClick: (Int) -> Unit,
    onUserClick: (Int) -> Unit,
    review: ReviewInfo,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onReviewClick(review.id.toInt()) },
            colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 1️⃣ Header: perfil
            ReviewCardHeader(
                userImage = review.userImage,
                userName = review.name,
                onUserClick = { onUserClick(review.userId.toInt()) }
            )
           // 2️⃣ Nombre del lugar arriba de la imagen
            Text(
                text = review.placeName,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )

            // 3️⃣ Imagen del lugar
            AsyncImage(
                model = review.placeImage,
                contentDescription = "Imagen del lugar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(MaterialTheme.shapes.medium)
            )

            // 4️⃣ Texto de la reseña
            ReviewCardBody(review.reviewText)

            // 5️⃣ Footer
            ReviewCardFooter(
                likes = review.likes,
                comments = review.comments,
                onLikeClick = { },
                onCommentClick = { },
                onShareClick = { }
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
            .clickable { onUserClick() } // 👈 acción de clic
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
//@Preview(showBackground = true)
//@Composable
//fun ReviewCardHeaderPreview() {
//    ReviewCardHeader(
//        userImage = "",
//        userName = "Carlos Perez",
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun ReviewCardFooterPreview() {
//    ReviewCardFooter(
//        likes = 120,
//        comments = 45,
//        onLikeClick = {},
//        onCommentClick = {},
//        onShareClick = {}
//    )
//}

//@Preview(showBackground = true)
//@Composable
//fun ReviewCardBodyPreview() {
//    ReviewCardBody(reviewText = "Un lugar muy acogedor con excelente café y atención al cliente.")
//}
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewReviewCard() {
//    val review = ReviewInfo(
//        userImage = "",
//        placeImage = "",
//        id = 1,
//        name = "Carlos Perez",
//        placeName = "Café del Parque",
//        reviewText = "Un lugar muy acogedor con excelente café y atención al cliente.",
//        likes = 120,
//        comments = 45,
//        //gastroBarId = 1
//    )
//    ReviewCard(
//        review = review,
//        onReviewClick = {},
//        modifier = Modifier
//    )
//}
