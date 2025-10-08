package com.example.myapplication.data.datasource

import com.example.myapplication.data.dtos.RegisterUserDto
import com.example.myapplication.data.dtos.UserDtoGeneric

interface UserRemoteDataSource {
    suspend fun getAllUsers(): List<UserDtoGeneric>
    suspend fun getUserById(id: Int): UserDtoGeneric
    suspend fun getUserByEmail(email: String): UserDtoGeneric
    suspend fun getUserByFirebaseUid(firebaseUid: String): UserDtoGeneric
    suspend fun createUser(user: UserDtoGeneric): Unit
    suspend fun updateUser(id: Int, user: UserDtoGeneric): Unit
    suspend fun deleteUser(id: Int): Unit
;
    suspend fun registerUser(registerUserDto: RegisterUserDto,userId:String): Unit
}
