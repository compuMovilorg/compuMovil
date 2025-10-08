package com.example.myapplication.data.datasource.impl.firestore

import com.example.myapplication.data.datasource.ReviewRemoteDataSource
import com.example.myapplication.data.dtos.CreateReviewDto
import com.example.myapplication.data.dtos.ReviewDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ReviewFireStoreDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
): ReviewRemoteDataSource  {
    override suspend fun getAllReviews(): List<ReviewDto> {
        return db.collection("reviews").get().await().toObjects(ReviewDto::class.java)
    }

    override suspend fun getReviewById(id: Int): ReviewDto {
        TODO("Not yet implemented")
    }

    override suspend fun createReview(review: CreateReviewDto) {
       db.collection("reviews").add(review).await()
    }

    override suspend fun deleteReview(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun updateReview(
        id: Int,
        review: CreateReviewDto
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun getReviewsReplies(id: Int): List<ReviewDto> {
        TODO("Not yet implemented")
    }

    override suspend fun getReviewsByUser(userId: Int): List<ReviewDto> {
        TODO("Not yet implemented")
    }

    override suspend fun getReviewsByGastroBar(gastroBarId: Int): List<ReviewDto> {
        TODO("Not yet implemented")
    }
}