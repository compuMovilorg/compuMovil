package com.example.myapplication.data.repository

import android.util.Log
import retrofit2.HttpException
import com.example.myapplication.data.ReviewInfo
import com.example.myapplication.data.datasource.AuthRemoteDataSource
import com.example.myapplication.data.datasource.UserRemoteDataSource
import com.example.myapplication.data.datasource.impl.firestore.ReviewFireStoreDataSourceImpl
import com.example.myapplication.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.myapplication.data.dtos.CreateReviewDto
import com.example.myapplication.data.dtos.CreateReviewUserDto
import com.example.myapplication.data.dtos.UserDtoGeneric
import com.example.myapplication.data.dtos.UserProfileDto
import com.example.myapplication.data.dtos.toReviewInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ReviewRepository @Inject constructor(
    private val reviewRemoteDataSource: ReviewFireStoreDataSourceImpl,
    private val userRemoteDataSource: UserFirestoreDataSourceImpl,
    private val authRemoteDataSource: AuthRemoteDataSource
) {

    suspend fun getReviews(): Result<List<ReviewInfo>> {
        return try {
            val reviews = reviewRemoteDataSource.getAllReviews()
            val reviewsInfo = reviews.map { it.toReviewInfo() }
            Result.success(reviewsInfo)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReviewById(id: String): Result<ReviewInfo> {
        val currentUserId = authRemoteDataSource.currentUser?.uid ?: ""

        return try {
            val review = reviewRemoteDataSource.getReviewById(id,currentUserId)
            Result.success(review.toReviewInfo())
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReviewsReplies(id: String): Result<List<ReviewInfo>> {
        return try {
            val reviews = reviewRemoteDataSource.getReviewsReplies(id)
            val reviewsInfo = reviews.map { it.toReviewInfo() }
            Result.success(reviewsInfo)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReviewsByUser(userId: String): Result<List<ReviewInfo>> {
        return try {
            val reviews = reviewRemoteDataSource.getReviewsByUser(userId)
            val reviewsInfo = reviews.map { it.toReviewInfo() }

            Log.d("ReviewRepository", "✅ Se obtuvieron ${reviewsInfo.size} reseñas del usuario $userId")
            Result.success(reviewsInfo)
        } catch (e: HttpException) {
            Log.e("ReviewRepository", "❌ HttpException ${e.code()} - ${e.message()}", e)
            Result.failure(e)
        } catch (e: Exception) {
            Log.e("ReviewRepository", "❌ Exception general: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun getReviewsByGastroBar(gastroBarId: String): Result<List<ReviewInfo>> {
        return try {
            val reviews = reviewRemoteDataSource.getReviewsByGastroBar(gastroBarId)
            val reviewsInfo = reviews.map { it.toReviewInfo() }

            Log.d("ReviewRepository", "✅ Se obtuvieron ${reviewsInfo.size} reseñas del gastrobar $gastroBarId")
            Result.success(reviewsInfo)
        } catch (e: HttpException) {
            Log.e("ReviewRepository", "❌ HttpException ${e.code()} - ${e.message()}", e)
            Result.failure(e)
        } catch (e: Exception) {
            Log.e("ReviewRepository", "❌ Exception general en getReviewsByGastroBar: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun createReview(createReviewDto: CreateReviewDto): Result<Unit> {
        val currentUserId = authRemoteDataSource.currentUser?.uid ?: ""

        return try {
            // Si el DTO ya trae user (UserProfileDto), lo usamos; si no, lo consultamos.
            val dtoWithUser = if (createReviewDto.user != null) {
                createReviewDto
            } else {
                // obtén data del usuario (puede lanzar excepción si no existe)
                val userDto = userRemoteDataSource.getUserById(createReviewDto.userId,currentUserId)
                val userProfile = UserProfileDto(
                    id = userDto.id,
                    username = userDto.username,
                    name = userDto.name,
                    profileImage = userDto.profileImage
                )
                createReviewDto.copy(user = userProfile)
            }

            // Llamamos al data source con el DTO completo (incluyendo placeImage si existe)
            reviewRemoteDataSource.createReview(dtoWithUser)

            Result.success(Unit)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



    suspend fun updateReview(id: String, review: CreateReviewDto): Result<Unit> {
        return try {
            reviewRemoteDataSource.updateReview(id, review)
            Result.success(Unit)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteReview(id: String): Result<Unit> {
        return try {
            reviewRemoteDataSource.deleteReview(id)
            Result.success(Unit)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendOrDeleteReviewLike(reviewId: String, userId: String): Result<Unit> {
        return try {
            reviewRemoteDataSource.sendOrDeleteReviewLike(reviewId, userId)
            Result.success(Unit)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReviewsLive(): Flow<List<ReviewInfo>> {
        return reviewRemoteDataSource.listenAllReviews().map { reviews ->
            reviews.map { it.toReviewInfo() }
        }
    }
}
