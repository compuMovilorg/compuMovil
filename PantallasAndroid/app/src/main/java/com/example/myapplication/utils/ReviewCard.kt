package com.example.myapplication.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.data.ReviewInfo

@Composable
fun ReviewCard(
    onReviewClick: (Int) -> Unit,
    review: ReviewInfo,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onReviewClick(review.id) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ReviewCardHeader(
                userImage = review.userImage,
                userName = review.name,
                placeName = review.placeName
            )

            Image(
                painter = painterResource(id = review.placeImage),
                contentDescription = "Imagen del lugar",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(MaterialTheme.shapes.medium)
            )

            ReviewCardBody(review.reviewText)

            ReviewCardFooter(
                likes = review.likes,
                comments = review.comments,
                onLikeClick = { /* Handle like click */ },
                onCommentClick = { /* Handle comment click */ },
                onShareClick = { /* Handle share click */ }
            )
        }
    }
}


@Composable
fun ReviewCardHeader(
    userImage: Int,
    userName: String,
    placeName: String
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = userImage),
                contentDescription = "User image",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = userName,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = placeName,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
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

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onShareClick) {
                Icon(Icons.Default.Share, contentDescription = "Compartir")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewCardHeaderPreview() {
    ReviewCardHeader(
        userImage = R.drawable.usr1,
        userName = "Carlos Perez",
        placeName = "Café del Parque"
    )
}

@Preview(showBackground = true)
@Composable
fun ReviewCardFooterPreview() {
    ReviewCardFooter(
        likes = 120,
        comments = 45,
        onLikeClick = {},
        onCommentClick = {},
        onShareClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun ReviewCardBodyPreview() {
    ReviewCardBody(reviewText = "Un lugar muy acogedor con excelente café y atención al cliente.")
}

@Preview(showBackground = true)
@Composable
fun PreviewReviewCard() {
    val review = ReviewInfo(
        userImage = R.drawable.usr1,
        placeImage = R.drawable.gastrobarimg1,
        id = 1,
        name = "Carlos Perez",
        placeName = "Café del Parque",
        reviewText = "Un lugar muy acogedor con excelente café y atención al cliente.",
        likes = 120,
        comments = 45,
        gastroBarId = 1
    )
    ReviewCard(
        review = review,
        onReviewClick = {},
        modifier = Modifier
    )
}
