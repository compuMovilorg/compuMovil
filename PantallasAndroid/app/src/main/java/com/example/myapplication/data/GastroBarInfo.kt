package com.example.myapplication.data

import androidx.annotation.DrawableRes

data class GastroBar(
    @DrawableRes val imagePlace: Int,
    val id: Int,
    val name: String,
    val rating: Float,
    val reviewCount: Int,
    val address: String,
    val hours: String,
    val cuisine: String,
    val description: String
)
