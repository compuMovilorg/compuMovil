package com.example.myapplication.data.repository

import android.util.Log
import retrofit2.HttpException
import com.example.myapplication.data.UserInfo
import com.example.myapplication.data.datasource.impl.UserRetrofitDataSourceImpl
import com.example.myapplication.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.myapplication.data.dtos.RegisterUserDto
import com.example.myapplication.data.dtos.UserDtoGeneric
import javax.inject.Inject
import kotlin.math.log

private const val TAG = "UserRepository"

class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserFirestoreDataSourceImpl
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

    // 🔹 Buscar por firebaseUid
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
    suspend fun registerUser(
        name: String,
        username: String,
        birthdate: String,
        userId: String
    ): Result<Unit> = runCatching {
        Log.d(TAG, "registerUser(params) -> name='$name', username='$username', birthdate='$birthdate', uid='$userId'")

        require(userId.isNotBlank()) { "UID de Firebase vacío" }
        require(!userId.contains("@")) { "UID inválido (parece un email): $userId" }

        val dto = RegisterUserDto(
            name = name.trim(),
            username = username.trim(),
            birthdate = birthdate.trim()
        )

        Log.d(TAG, "registerUser(dto) -> $dto")

        userRemoteDataSource.registerUser(dto, userId)

        Log.d(TAG, "registerUser: saved OK at users/$userId")

        // 👇 Fuerza que el bloque retorne Unit (no Int)
        Unit
    }.onFailure { e ->
        Log.e(TAG, "registerUser error: ${e.javaClass.simpleName}: ${e.message}", e)
    }
}
