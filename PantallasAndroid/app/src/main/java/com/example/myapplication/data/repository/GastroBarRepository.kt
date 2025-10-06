package com.example.myapplication.data.repository

import com.example.myapplication.data.datasource.impl.GastroBarRetrofitDataSourceImpl
import com.example.myapplication.data.dtos.CreateGastroBarDto
import com.example.myapplication.data.dtos.GastroBarDto
import com.example.myapplication.data.dtos.toGastroBarInfo
import com.example.myapplication.data.GastroBar
import retrofit2.HttpException
import javax.inject.Inject

class GastroBarRepository @Inject constructor(
    private val gastroBarRemoteDataSource: GastroBarRetrofitDataSourceImpl
) {
    suspend fun getGastroBares(): Result<List<GastroBar>> {
        return try {
            val gastrobares = gastroBarRemoteDataSource.getAllGastroBares()
            val gastroBarInfo = gastrobares.map { it.toGastroBarInfo() }
            Result.success(gastroBarInfo)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getGastroBarById(id: Int): Result<GastroBar> {
        return try {
            val gastrobar = gastroBarRemoteDataSource.getGastroBarById(id)
            Result.success(gastrobar.toGastroBarInfo())
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createGastroBar(
        imagePlace: String?,
        name: String,
        rating: Float = 0.0f,
        reviewCount: Int = 0,
        address: String,
        hours: String?,
        cuisine: String?,
        description: String?,
        reviewId: Int?
    ): Result<Unit> {
        return try {
            val dto = CreateGastroBarDto(
                imagePlace = imagePlace,
                name = name,
                rating = rating,
                reviewCount = reviewCount,
                address = address,
                hours = hours,
                cuisine = cuisine,
                description = description,
                reviewId = reviewId
            )
            gastroBarRemoteDataSource.createGastroBar(dto)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateGastroBar(id: Int, dto: CreateGastroBarDto): Result<Unit> {
        return try {
            gastroBarRemoteDataSource.updateGastroBar(id, dto)
            Result.success(Unit)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteGastroBar(id: Int): Result<Unit> {
        return try {
            gastroBarRemoteDataSource.deleteGastroBar(id)
            Result.success(Unit)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
