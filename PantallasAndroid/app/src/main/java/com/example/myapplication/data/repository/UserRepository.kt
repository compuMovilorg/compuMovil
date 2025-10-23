package com.example.myapplication.data.repository

import android.util.Log
import com.example.myapplication.data.UserInfo
import com.example.myapplication.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.myapplication.data.dtos.RegisterUserDto
import com.example.myapplication.data.dtos.UserFirestoreDto
import com.google.firebase.auth.FirebaseAuth
import retrofit2.HttpException
import javax.inject.Inject

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

    // Buscar por email (mismo patrón de try/catch que el resto)
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

    // Buscar por firebaseUid
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
    suspend fun createUser(user: RegisterUserDto, userId: String): Result<Unit> {
        return try {
            userRemoteDataSource.registerUser(user, userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --------------------- Actualización de usuario ---------------------
    /**
     * Actualiza el usuario en backend usando un UserInfo. Convertimos a DTO de Firestore
     * para que el data source reciba lo que espera.
     */
    suspend fun updateUser(user: UserInfo): Result<Unit> {
        return try {
            // Construimos UserFirestoreDto con valores por defecto para followers/following si no los tienes
            val dto = UserFirestoreDto(
                id = user.id,
                username = user.username ?: "",
                email = user.email ?: "",
                password = "", // si no aplicable, dejar vacío; backend no debería usarlo para actualización
                name = user.name ?: "",
                birthdate = user.birthdate ?: "",
                followersCount = user.followersCount ?: 0,
                followingCount = user.followingCount ?: 0,
                profileImage = user.profileImage
            )

            userRemoteDataSource.updateUser(user.id, dto)
            Result.success(Unit)
        } catch (e: HttpException) {
            Log.w(TAG, "updateUser http error: ${e.message}")
            Result.failure(e)
        } catch (e: Exception) {
            Log.w(TAG, "updateUser error: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * Actualiza únicamente la URL de la imagen de perfil para el usuario `id`.
     * Tu ViewModel llama a userRepository.updateProfileImage(id, url) — lo soportamos aquí.
     */
    suspend fun updateProfileImage(id: String, profileImageUrl: String): Result<Unit> {
        return try {
            userRemoteDataSource.updateProfileImage(id, profileImageUrl)
            Result.success(Unit)
        } catch (e: HttpException) {
            Log.w(TAG, "updateProfileImage http error: ${e.message}")
            Result.failure(e)
        } catch (e: Exception) {
            Log.w(TAG, "updateProfileImage error: ${e.message}", e)
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

    // Registro con logging tal como lo tenías
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

        Unit
    }.onFailure { e ->
        Log.e(TAG, "registerUser error: ${e.javaClass.simpleName}: ${e.message}", e)
    }

    // -------------------- ADICIONES PARA resolver tu error --------------------

    /**
     * Devuelve el uid del usuario autenticado en FirebaseAuth, o null si no hay sesión.
     * Lo dejamos como `suspend` para que puedas llamarlo desde coroutines sin cambios.
     */
    suspend fun getCurrentUserId(): String? {
        return try {
            FirebaseAuth.getInstance().currentUser?.uid
        } catch (e: Exception) {
            Log.w(TAG, "getCurrentUserId: error obteniendo FirebaseAuth.currentUser", e)
            null
        }
    }

    /**
     * Helper: devuelve el UserInfo del usuario autenticado (usando firebase uid)
     * Resultado: Result.success(UserInfo) o Result.failure(...)
     */
    suspend fun getCurrentUser(): Result<UserInfo> {
        val uid = getCurrentUserId()
        return if (uid.isNullOrBlank()) {
            Result.failure(IllegalStateException("No hay usuario autenticado"))
        } else {
            getUserByFirebaseUid(uid)
        }
    }
}
