package com.example.myapplication.data

import androidx.annotation.DrawableRes

data class AppInfo(
    @DrawableRes val userImage: Int,
    val name: String,
    val placeName: String,
    val reviewText: String,
    val likes: Int,
    val comments: Int
)

data class GastroBar(
    val name: String,
    val rating: Float,
    val reviewCount: Int,
    val address: String,
    val hours: String,
    val cuisine: String,
    val description: String
)

data class UserReview(
    val userName: String,
    val userImage: Int,
    val timeAgo: String,
    val rating: Float,
    val placeName: String,
    val reviewText: String
)
