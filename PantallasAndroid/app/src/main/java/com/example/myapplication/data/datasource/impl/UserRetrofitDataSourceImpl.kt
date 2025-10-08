package com.example.myapplication.data.datasource.impl

import com.example.myapplication.data.datasource.UserRemoteDataSource
import com.example.myapplication.data.datasource.services.UserRetrofitService
import com.example.myapplication.data.dtos.RegisterUserDto
import com.example.myapplication.data.dtos.UserDtoGeneric
import com.example.myapplication.data.dtos.UserRetrofitDto
import javax.inject.Inject

class UserRetrofitDataSourceImpl @Inject constructor(
    private val service: UserRetrofitService
) : UserRemoteDataSource {
    override suspend fun getAllUsers(): List<UserDtoGeneric> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserById(id: String): UserDtoGeneric {
        TODO("Not yet implemented")
    }

    override suspend fun getUserByEmail(email: String): UserDtoGeneric {
        TODO("Not yet implemented")
    }

    override suspend fun getUserByFirebaseUid(firebaseUid: String): UserDtoGeneric {
        TODO("Not yet implemented")
    }

    override suspend fun createUser(user: UserDtoGeneric) {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(
        id: String,
        user: UserDtoGeneric
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun registerUser(
        registerUserDto: RegisterUserDto,
        userId: String
    ) {
        TODO("Not yet implemented")
    }

}
