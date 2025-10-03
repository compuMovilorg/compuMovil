package com.example.myapplication.data.datasource

import com.example.myapplication.data.ReviewInfo

interface ReviewRemoteDataSource {
    suspend fun getAllReviews(): List<ReviewInfo>
}