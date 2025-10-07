package com.example.myapplication.data.dtos

import com.example.myapplication.data.ReviewInfo

data class ReviewDto(
    val id: Int,
    val userId: Int,
    val placeName: String,
    val imagePlace: String?,   // URL de la imagen del gastrobar
    val reviewText: String,
    val likes: Int = 0,
    val comments: Int = 0,
    val parentReviewId: Int? = null,
    val createdAt: String,
    val updatedAt: String,
    val user: UserDto?,          // Relación con el usuario
    val gastroBar: GastroBarDto?
)

// Mapper de DTO → Info (para usar en UI)
fun ReviewDto.toReviewInfo(): ReviewInfo {
    return ReviewInfo(
        id = id,
        userId = user?.id ?: userId,
        userImage = user?.profileImage ?: "",
        placeImage = gastroBar?.imagePlace ?: imagePlace ?: "",
        name = user?.name ?: "Usuario",                         // evita NPE
        placeName = gastroBar?.name ?: placeName,
        reviewText = reviewText,
        likes = likes,
        comments = comments
    )
}
