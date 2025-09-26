package com.example.myapplication.data

import androidx.annotation.DrawableRes

data class ReviewInfo(
    @DrawableRes val userImage: Int,
    @DrawableRes val placeImage: Int,
    val id: Int,
    val name: String,
    val placeName: String,
    val reviewText: String,
    val likes: Int,
    val comments: Int,
)
