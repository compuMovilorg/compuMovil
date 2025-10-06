package com.example.myapplication.data.dtos

data class CreateReviewDto (
    val userId: Int,
    val placeName: String,
    val reviewText: String,
    val parentReviewId:Int?,
)