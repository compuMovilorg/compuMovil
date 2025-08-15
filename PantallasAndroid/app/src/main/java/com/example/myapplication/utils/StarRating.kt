package com.example.myapplication.utils

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun StarRating(rating: Float, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        val fullStars = rating.toInt()
        val hasHalfStar = rating - fullStars >= 0.5f
        repeat(fullStars) {
            Icon(imageVector = Icons.Default.Star, contentDescription = "Full Star", tint = Color.Yellow)
        }
        if (hasHalfStar) {
            Icon(imageVector = Icons.Default.StarHalf, contentDescription = "Half Star", tint = Color.Yellow)
        }
    }
}