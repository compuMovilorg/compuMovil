package com.example.myapplication.data.dtos

import com.example.myapplication.data.GastroBar

data class GastroBarDto(
    val id: String,
    val imagePlace: String?,  // URL de la imagen
    val name: String,
    val rating: Float,
    val reviewCount: Int,
    val address: String,
    val hours: String,
    val cuisine: String,
    val description: String,
    val reviewId: Int?        // FK con Review
){
    constructor(): this("", null, "", 0f, 0, "", "", "", "", null)
}

// Mapper DTO → Info (para UI)
fun GastroBarDto.toGastroBarInfo(): GastroBar {
    return GastroBar(
        id = id,
        imagePlace = imagePlace,
        name = name,
        rating = rating,
        reviewCount = reviewCount,
        address = address,
        hours = hours,
        cuisine = cuisine,
        description = description
    )
}
