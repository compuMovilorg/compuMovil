package com.example.myapplication.data.datasource.services

import com.example.myapplication.data.dtos.UserRetrofitDto
import retrofit2.http.*

interface UserRetrofitService {

    @GET("users")
    suspend fun getAllUsers(): List<UserRetrofitDto>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): UserRetrofitDto

    @POST("users")
    suspend fun createUser(@Body user: UserRetrofitDto): UserRetrofitDto

    @PUT("users/{id}")
    suspend fun updateUser(
        @Path("id") id: Int,
        @Body user: UserRetrofitDto
    ): UserRetrofitDto

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: Int)

    // Buscar usuario por email
    @GET("users/by-email")
    suspend fun getUserByEmail(@Query("email") email: String): UserRetrofitDto

    // Buscar usuario por firebaseUid
    @GET("users/by-firebase-uid")
    suspend fun getUserByFirebaseUid(@Query("firebaseUid") firebaseUid: String): UserRetrofitDto
}
