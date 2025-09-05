package com.example.myapplication.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.floor

@Composable
fun StarRating(
    rating: Float,
    modifier: Modifier = Modifier,
    maxStars: Int = 5,
    onRatingChanged: ((Int) -> Unit)? = null
) {
    val starColor = Color(0xFFFFC107)

    Row(modifier = modifier) {
        val fullStars = floor(rating).toInt()
        val hasHalfStar = (rating - fullStars) >= 0.5f

        for (i in 1..maxStars) {
            val icon = when {
                i <= fullStars -> Icons.Default.Star
                i == fullStars + 1 && hasHalfStar -> Icons.Default.StarHalf
                else -> Icons.Default.StarOutline
            }

            Icon(
                imageVector = icon,
                contentDescription = "Star $i",
                tint = starColor,
                modifier = if (onRatingChanged != null) {
                    Modifier.clickable { onRatingChanged(i) }
                } else {
                    Modifier
                }
            )
        }
    }
}

//@Composable
//@Preview(showBackground = true)
//fun StarRatingPreview(){
//    StarRating(
//        modifier = Modifier
//    )
//}

