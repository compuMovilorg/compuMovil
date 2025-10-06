package com.example.myapplication.data.datasource

import com.example.myapplication.data.dtos.UserDto

interface UserRemoteDataSource {
    suspend fun getAllUsers(): List<UserDto>
    suspend fun getUserById(id: Int): UserDto
    suspend fun createUser(user: UserDto): Unit
    suspend fun updateUser(id: Int, user: UserDto): Unit
    suspend fun deleteUser(id: Int): Unit
}
