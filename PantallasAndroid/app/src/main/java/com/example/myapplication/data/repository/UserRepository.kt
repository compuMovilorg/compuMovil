package com.example.myapplication.data.repository

import retrofit2.HttpException
import com.example.myapplication.data.UserInfo
import com.example.myapplication.data.datasource.impl.UserRetrofitDataSourceImpl
import com.example.myapplication.data.dtos.UserDto
import com.example.myapplication.data.dtos.UserProfileDto
import com.example.myapplication.data.dtos.toUserInfo
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserRetrofitDataSourceImpl
) {

    // Obtener todos los usuarios (lista general)
    suspend fun getUsers(): Result<List<UserInfo>> {
        return try {
            val users = userRemoteDataSource.getAllUsers()
            val usersInfo = users.map { it.toUserInfo() }
            Result.success(usersInfo)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Obtener un usuario por ID
    suspend fun getUserById(id: Int): Result<UserInfo> {
        return try {
            val user = userRemoteDataSource.getUserById(id)
            Result.success(user.toUserInfo())
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 🔹 Buscar por email (mismo patrón de try/catch que el resto)
    suspend fun getUserByEmail(email: String): Result<UserInfo> {
        return try {
            val user = userRemoteDataSource.getUserByEmail(email)
            Result.success(user.toUserInfo())
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 🔹 Buscar por firebaseUid (mismo patrón de try/catch que el resto)
    suspend fun getUserByFirebaseUid(firebaseUid: String): Result<UserInfo> {
        return try {
            val user = userRemoteDataSource.getUserByFirebaseUid(firebaseUid)
            Result.success(user.toUserInfo())
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Crear un usuario general
    suspend fun createUser(user: UserDto): Result<Unit> {
        return try {
            userRemoteDataSource.createUser(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Crear/actualizar perfil del usuario logueado
    suspend fun updateUserProfile(id: Int, profile: UserProfileDto): Result<Unit> {
        return try {
            // Se asume que el data source maneja UserProfileDto igual que UserDto
            userRemoteDataSource.updateUser(id, profile as UserDto)
            Result.success(Unit)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Eliminar usuario
    suspend fun deleteUser(id: Int): Result<Unit> {
        return try {
            userRemoteDataSource.deleteUser(id)
            Result.success(Unit)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
