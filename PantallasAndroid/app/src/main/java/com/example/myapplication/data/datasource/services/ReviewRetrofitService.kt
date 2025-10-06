package com.example.myapplication.data.datasource.services

import com.example.myapplication.data.dtos.CreateReviewDto
import com.example.myapplication.data.dtos.ReviewDto
import retrofit2.http.*

interface ReviewRetrofitService {
    @GET("reviews")
    suspend fun getAllReviews(): List<ReviewDto>

    @GET("reviews/{id}")
    suspend fun getReviewById(@Path("id") id: Int): ReviewDto

    @POST("reviews")
    suspend fun createReview(@Body review: CreateReviewDto): ReviewDto

    @PUT("reviews/{id}")
    suspend fun updateReview(
        @Path("id") id: Int,
        @Body review: CreateReviewDto
    ): ReviewDto

    @DELETE("reviews/{id}")
    suspend fun deleteReview(@Path("id") id: Int)

    @GET("reviews/{id}/replies")
    suspend fun getReviewsReplies(@Path("id")id: Int): List<ReviewDto>
}

