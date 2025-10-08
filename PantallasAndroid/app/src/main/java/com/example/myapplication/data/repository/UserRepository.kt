package com.example.myapplication.data.repository

import android.util.Log
import retrofit2.HttpException
import com.example.myapplication.data.UserInfo
import com.example.myapplication.data.datasource.impl.UserRetrofitDataSourceImpl
import com.example.myapplication.data.datasource.impl.firestore.userFirestoreDataSourceImpl
import com.example.myapplication.data.dtos.RegisterUserDto
import com.example.myapplication.data.dtos.UserDtoGeneric
import javax.inject.Inject
import kotlin.math.log

class UserRepository @Inject constructor(
    private val userRemoteDataSource: userFirestoreDataSourceImpl
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
    suspend fun getUserById(id: String): Result<UserInfo> {
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
    suspend fun createUser(user: UserDtoGeneric): Result<Unit> {
        return try {
            userRemoteDataSource.createUser(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Crear/actualizar perfil del usuario logueado
    suspend fun updateUserProfile(id: String, profile: UserDtoGeneric): Result<Unit> {
        return try {
            // Se asume que el data source maneja UserProfileDto igual que UserDto
            userRemoteDataSource.updateUser(id, profile) // revisar
            Result.success(Unit)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Eliminar usuario
    suspend fun deleteUser(id: String): Result<Unit> {
        return try {
            userRemoteDataSource.deleteUser(id)
            Result.success(Unit)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun registerUser( name: String,  username: String, birthdate: String, userId: String
    ): Result<Unit> {
        return try {
            val registerUserDto = RegisterUserDto(name, username, birthdate)
            userRemoteDataSource.registerUser(registerUserDto, userId)
            Result.success(Unit)
        } catch (e: HttpException) {
            Log.d("UserRepository", "registerUser: HttpException: ${e.message()}")
            Result.failure(e)
        } catch (e: Exception) {
            Log.e("UserRepository", "registerUser: Exception: ${e.message}", e)
            Result.failure(e)
        }
    }
}
