package com.example.myapplication.data.repository

import com.example.myapplication.data.datasource.AuthRemoteDataSource
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val userRepository: UserRepository // <-- INYECTA
) {
    val currentUser: FirebaseUser?
        get() = authRemoteDataSource.currentUser

    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            authRemoteDataSource.login(email, password)
            // Upsert del perfil en Firestore con los datos mínimos que tengas
            val uid = currentUser?.uid ?: return Result.failure(IllegalStateException("Auth ok pero sin currentUser"))
            // Coloca aquí los campos que quieras guardar (puedes pedirlos a la UI)
            userRepository.registerUser(
                name = currentUser?.displayName ?: "",
                username = email.substringBefore("@"),
                birthdate = "", // si lo tienes
                userId = uid // MUY IMPORTANTE: usar uid
            ).getOrThrow()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(email: String, password: String): Result<Unit> {
        return try {
            authRemoteDataSource.register(email, password)
            val uid = currentUser?.uid ?: return Result.failure(IllegalStateException("Auth ok pero sin currentUser"))
            userRepository.registerUser(
                name = currentUser?.displayName ?: "",
                username = email.substringBefore("@"),
                birthdate = "",
                userId = uid
            ).getOrThrow()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        authRemoteDataSource.logout()
    }
}
