package com.example.myapplication.data.datasource

import com.example.myapplication.data.dtos.RegisterUserDto
import com.example.myapplication.data.dtos.UserDtoGeneric

interface UserRemoteDataSource {

    suspend fun getAllUsers(): List<UserDtoGeneric>

    suspend fun getUserById(id: String,currentUserId: String): UserDtoGeneric

    suspend fun getUserByEmail(email: String): UserDtoGeneric

//    suspend fun getUserByFirebaseUid(firebaseUid: String): UserDtoGeneric

    suspend fun createUser(user: UserDtoGeneric)

    suspend fun updateUser(id: String, user: UserDtoGeneric)

    suspend fun deleteUser(id: String)

    suspend fun registerUser(registerUserDto: RegisterUserDto, userId: String)

    suspend fun updateProfileImage(id: String, profileImageUrl: String)

    suspend fun followOrUnfollowUser(currentUserId: String, targetUserId: String): Result<Unit>
}
