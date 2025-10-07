package com.example.myapplication.data.datasource

import com.example.myapplication.data.dtos.CreateReviewDto
import com.example.myapplication.data.dtos.ReviewDto

interface ReviewRemoteDataSource {
    suspend fun getAllReviews(): List<ReviewDto>
    suspend fun getReviewById(id: Int): ReviewDto
    suspend fun createReview(review: CreateReviewDto)
    suspend fun deleteReview(id: Int)
    suspend fun updateReview(id: Int, review: CreateReviewDto)
    suspend fun getReviewsReplies(id: Int): List<ReviewDto>
    suspend fun getReviewsByUser(userId: Int): List<ReviewDto>
    suspend fun getReviewsByGastroBar(gastroBarId: Int): List<ReviewDto>
}
