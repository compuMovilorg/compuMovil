package com.example.myapplication.data.datasource

import com.example.myapplication.data.ReviewInfo
import com.example.myapplication.data.dtos.CreateReviewDto
import com.example.myapplication.data.dtos.ReviewDto

interface ReviewRemoteDataSource {
    suspend fun getAllReviews(): List<ReviewDto>
    suspend fun getReviewById(id:Int): ReviewDto
    suspend fun createReview(review: CreateReviewDto): Unit
    suspend fun deleteReview(id: Int): Unit
    suspend fun updateReview(id: Int, review: CreateReviewDto): Unit
    suspend fun getReviewsReplies(id: Int): List<ReviewDto>
}