package com.example.myapplication.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R

@Composable
fun ReviewCard(
    userImage: Painter,
    userName: String,
    placeName: String,
    reviewText: String,
    likes: Int,
    comments: Int,
    placeImage: Painter,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ReviewCardHeader(userImage, userName, placeName)

            Image(
                painter = placeImage,
                contentDescription = "Imagen del lugar",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp) // tamaño ajustable
                    .clip(MaterialTheme.shapes.medium)
            )

            ReviewCardBody(reviewText)
            ReviewCardFooter(likes, comments)
        }
    }
}

@Composable
private fun ReviewCardHeader(
    userImage: Painter,
    userName: String,
    placeName: String
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = userImage,
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
private fun ReviewCardBody(reviewText: String) {
    Text(
        text = reviewText,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
private fun ReviewCardFooter(likes: Int, comments: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Favorite, contentDescription = "Likes")
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "$likes")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Comment, contentDescription = "Comments")
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "$comments")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewReviewCard() {
    ReviewCard(
        userImage = painterResource(id = R.drawable.usr1),
        userName = "Carlos Perez",
        placeName = "Café del Parque",
        reviewText = "Un lugar muy acogedor con excelente café y atención al cliente.",
        likes = 120,
        comments = 45,
        placeImage = painterResource(id = R.drawable.gastrobarimg1)
    )
}
