package com.example.myapplication.data

import androidx.annotation.DrawableRes

data class ReviewInfo(
    val userImage: String?,
    val placeImage: String?,
    val userId: String,
    val id: String,
    val name: String?,
    val placeName: String,
    val reviewText: String,
    val likes: Int,
    val comments: Int,
    val gastroBarId: String? = null,
    val liked: Boolean = false
)

//    val placeId: Int,                  // equivalente a gastroBarId
//    val parentReviewId: Int? = null,   // null si es review principal
//    val replies: List<ReviewInfo> = emptyList()  // para respuestas
