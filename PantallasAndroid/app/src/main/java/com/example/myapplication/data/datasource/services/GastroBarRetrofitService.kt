package com.example.myapplication.data.datasource.services

import com.example.myapplication.data.dtos.CreateGastroBarDto
import com.example.myapplication.data.dtos.GastroBarDto
import retrofit2.http.*

interface GastroBarRetrofitService {
    @GET("gastrobars")
    suspend fun getAllGastroBares(): List<GastroBarDto>

    @GET("gastrobars/{id}")
    suspend fun getGastroBarById(@Path("id") id: Int): GastroBarDto

    @POST("gastrobars")
    suspend fun createGastroBar(@Body gastrobar: CreateGastroBarDto): GastroBarDto

    @PUT("gastrobars/{id}")
    suspend fun updateGastroBar(
        @Path("id") id: Int,
        @Body gastrobar: CreateGastroBarDto
    ): GastroBarDto

    @DELETE("gastrobars/{id}")
    suspend fun deleteGastroBar(@Path("id") id: Int)
}
