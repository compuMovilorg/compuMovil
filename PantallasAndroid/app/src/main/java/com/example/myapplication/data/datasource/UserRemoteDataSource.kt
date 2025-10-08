package com.example.myapplication.data.datasource

import com.example.myapplication.data.dtos.RegisterUserDto
import com.example.myapplication.data.dtos.UserDtoGeneric

interface UserRemoteDataSource {
    suspend fun getAllUsers(): List<UserDtoGeneric>
    suspend fun getUserById(id: String): UserDtoGeneric
    suspend fun getUserByEmail(email: String): UserDtoGeneric
    suspend fun getUserByFirebaseUid(firebaseUid: String): UserDtoGeneric
    suspend fun createUser(user: UserDtoGeneric): Unit
    suspend fun updateUser(id: String, user: UserDtoGeneric): Unit
    suspend fun deleteUser(id: String): Unit
;
    suspend fun registerUser(registerUserDto: RegisterUserDto,userId:String): Unit
}
