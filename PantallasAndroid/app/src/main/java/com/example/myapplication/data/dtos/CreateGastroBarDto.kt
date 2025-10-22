package com.example.myapplication.data.dtos

data class CreateGastroBarDto(
    val imagePlace: String?,
    val name: String,
    val rating: Float = 0.0f,   // valor por defecto
    val reviewCount: Int = 0,   // valor por defecto
    val address: String,
    val hours: String?,
    val cuisine: String?,
    val description: String?,
    val reviewId: String?          // relación con una review
)
