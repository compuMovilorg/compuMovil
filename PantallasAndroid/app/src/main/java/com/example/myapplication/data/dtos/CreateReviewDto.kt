package com.example.myapplication.data.dtos

data class CreateReviewUserDto(
    val username: String,
    val name: String,
    val profileImage: String?
)
data class CreateReviewDto (
    val userId: String,
    val placeName: String,
    val reviewText: String,
    val parentReviewId: String?,
    val placeImage: String?,

    val user: UserProfileDto? = null,
    val gastroBarId: String? = null
)