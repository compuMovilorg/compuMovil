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

    override suspend fun getAllUsers(): List<UserRetrofitDto> {
        return service.getAllUsers()
    }

    override suspend fun getUserById(id: Int): UserRetrofitDto {
        return service.getUserById(id)
    }

//    override suspend fun createUser(user: UserRetrofitDto) {
//        service.createUser(user)
//    }
//
//    override suspend fun updateUser(id: Int, user: UserRetrofitDto) {
//        service.updateUser(id, user)
//    }

    override suspend fun deleteUser(id: Int) {
        service.deleteUser(id)
    }

    override suspend fun registerUser(
        registerUserDto: RegisterUserDto,
        userId: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun getUserByEmail(email: String): UserRetrofitDto {
        return service.getUserByEmail(email)
    }

    override suspend fun getUserByFirebaseUid(firebaseUid: String): UserRetrofitDto {
        return service.getUserByFirebaseUid(firebaseUid)
    }

    override suspend fun createUser(user: UserDtoGeneric) {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(
        id: Int,
        user: UserDtoGeneric
    ) {
        TODO("Not yet implemented")
    }
}
