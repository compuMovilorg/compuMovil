package com.example.myapplication.data.dtos

import com.example.myapplication.data.ReviewInfo

data class userProfileDto(
    val id: String,
    val username: String,
    val name: String,
    val profileImage: String?
){
    constructor(): this("", "", "", null)
}


data class ReviewDto(
    val id: String,
    val userId: String,
    val placeName: String,
    val imagePlace: String?,   // URL de la imagen del gastrobar
    val reviewText: String,
    val likes: Int = 0,
    val comments: Int = 0,
    val parentReviewId: Int? = null,
    val createdAt: String,
    val updatedAt: String,
    val user: userProfileDto,          // Relación con el usuario
    val gastroBar: GastroBarDto?
){
    constructor(): this("", "", "", null, "", 0, 0, null, "", "", userProfileDto(), null)
}

// Mapper de DTO → Info (para usar en UI)
fun ReviewDto.toReviewInfo(): ReviewInfo {
    return ReviewInfo(
        id = id,
        userId = userId,
        userImage = user?.profileImage ?: "",
        placeImage = gastroBar?.imagePlace ?: imagePlace ?: "",
        name = user?.name ?: "Usuario",
        placeName = gastroBar?.name ?: placeName,
        reviewText = reviewText,
        likes = likes,
        comments = comments
    )
}

