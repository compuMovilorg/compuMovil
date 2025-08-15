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
