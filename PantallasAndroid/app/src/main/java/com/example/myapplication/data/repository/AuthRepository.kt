package com.example.myapplication.data.repository

import com.example.myapplication.data.datasource.AuthRemoteDataSource
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject
import kotlinx.coroutines.withTimeout

class AuthRepository @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource
) {
    val currentUser: FirebaseUser?
        get() = authRemoteDataSource.currentUser

    /** Inicia sesión. NO escribe perfil. */
    suspend fun login(email: String, password: String): Result<Unit> = runCatching {
        withTimeout(15_000) {
            authRemoteDataSource.login(email, password)
        }
        Unit
    }

    /**
     * Registra al usuario y devuelve el UID NUEVO.
     * NO escribe perfil; el ViewModel decide cuándo/qué guardar en Firestore.
     */
    suspend fun register(email: String, password: String): Result<String> = runCatching {
        withTimeout(15_000) {
            authRemoteDataSource.register(email, password) // ← devuelve UID
        }
    }

    fun logout() = authRemoteDataSource.logout()
}
