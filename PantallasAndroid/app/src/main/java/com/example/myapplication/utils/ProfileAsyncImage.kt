package com.example.myapplication.utils

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.myapplication.R

@Composable
fun ProfileAsyncImage(
    profileImage: String,
    size: Int,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(profileImage)
            .crossfade(true)
            .build(),
        error = painterResource(id = R.drawable.user_image_icon),
        placeholder = painterResource(id = R.drawable.loading_img),
        contentDescription = "Profile Picture",
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}
