package com.example.myapplication.data.datasource.impl

import com.example.myapplication.data.datasource.UserRemoteDataSource
import com.example.myapplication.data.datasource.services.UserRetrofitService
import com.example.myapplication.data.dtos.UserDto
import javax.inject.Inject

class UserRetrofitDataSourceImpl @Inject constructor(
    private val service: UserRetrofitService
) : UserRemoteDataSource {

    override suspend fun getAllUsers(): List<UserDto> {
        return service.getAllUsers()
    }

    override suspend fun getUserById(id: Int): UserDto {
        return service.getUserById(id)
    }

    override suspend fun createUser(user: UserDto) {
        service.createUser(user)
    }

    override suspend fun updateUser(id: Int, user: UserDto) {
        service.updateUser(id, user)
    }

    override suspend fun deleteUser(id: Int) {
        service.deleteUser(id)
    }
}
