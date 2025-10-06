package com.example.myapplication.data.dtos

import com.example.myapplication.data.ReviewInfo

data class ReviewDto(
    val id: Int,
    val userId: Int,
    val placeName: String,
    val placeImage: String?,   // URL de la imagen del gastrobar
    val reviewText: String,
    val likes: Int = 0,
    val comments: Int = 0,
    val parentReviewId: Int?,
    val createdAt: String,
    val updatedAt: String,
    val user: UserDto          // Relación con el usuario
)

// Mapper de DTO → Info (para usar en UI)
fun ReviewDto.toReviewInfo(): ReviewInfo {
    return ReviewInfo(
        id = id,
        userImage = user.profileImage,
        placeImage = placeImage,
        name = user.name,
        placeName = placeName,
        reviewText = reviewText,
        likes = likes,
        comments = comments
    )
}
