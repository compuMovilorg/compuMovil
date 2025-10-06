package com.example.myapplication.data

import androidx.annotation.DrawableRes

data class ReviewInfo(
    val userImage: String?,
    val placeImage: String?,
    val id: Int,
    val name: String?,
    val placeName: String,
    val reviewText: String,
    val likes: Int,
    val comments: Int,
//    val placeId: Int,                  // equivalente a gastroBarId
//    val parentReviewId: Int? = null,   // null si es review principal
//    val replies: List<ReviewInfo> = emptyList()  // para respuestas
)