package com.example.myapplication.data.datasource.impl

import com.example.myapplication.data.datasource.ReviewRemoteDataSource
import com.example.myapplication.data.datasource.services.ReviewRetrofitService
import com.example.myapplication.data.dtos.CreateReviewDto
import com.example.myapplication.data.dtos.ReviewDto
import javax.inject.Inject

class ReviewRetrofitDataSourceImpl @Inject constructor(
    private val service: ReviewRetrofitService
) : ReviewRemoteDataSource {

    override suspend fun getAllReviews(): List<ReviewDto> {
        return service.getAllReviews()
    }

    override suspend fun getReviewById(id: String): ReviewDto {
        return service.getReviewById(id)
    }

    override suspend fun createReview(review: CreateReviewDto) {
        service.createReview(review)
    }

    override suspend fun deleteReview(id: String) {
        service.deleteReview(id)
    }

    override suspend fun updateReview(
        id: String,
        review: CreateReviewDto
    ) {
        service.updateReview(id, review)
    }

    override suspend fun getReviewsReplies(id: String): List<ReviewDto> {
        return service.getReviewsReplies(id)
    }

    override suspend fun getReviewsByUser(userId: String): List<ReviewDto> {
        return service.getReviewsByUser(userId)
    }

    override suspend fun getReviewsByGastroBar(gastroBarId: String): List<ReviewDto> {
        return service.getReviewsByGastroBar(gastroBarId)
    }
}
