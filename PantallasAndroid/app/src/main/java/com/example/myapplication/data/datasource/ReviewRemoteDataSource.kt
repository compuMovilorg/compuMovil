package com.example.myapplication.data.datasource

import com.example.myapplication.data.dtos.CreateReviewDto
import com.example.myapplication.data.dtos.ReviewDto
import kotlinx.coroutines.flow.Flow

interface ReviewRemoteDataSource {
    suspend fun getAllReviews(): List<ReviewDto>
    suspend fun getReviewById(id: String, currentUserId: String = ""): ReviewDto
    suspend fun createReview(review: CreateReviewDto)
    suspend fun deleteReview(id: String)
    suspend fun updateReview(id: String, review: CreateReviewDto)
    suspend fun getReviewsReplies(id: String): List<ReviewDto>
    suspend fun getReviewsByUser(userId: String): List<ReviewDto>
    suspend fun getReviewsByGastroBar(gastroBarId: String): List<ReviewDto>

    suspend fun sendOrDeleteReviewLike(reviewId: String, userId: String): Unit

    suspend fun listenAllReviews(): Flow<List<ReviewDto>>

}
