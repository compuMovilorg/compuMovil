package com.example.myapplication.data.repository

import com.example.myapplication.data.datasource.AuthRemoteDataSource
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

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
            authRemoteDataSource.register(email, password)
        }
    }

    fun logout() = authRemoteDataSource.logout()

    /**
     * Actualiza la foto de perfil del usuario en FirebaseAuth.
     */
    suspend fun updateProfileImage(url: String): Result<Unit> = runCatching {
        withTimeout(10_000) {
            authRemoteDataSource.updateProfileImage(url)
        }
    }

    /**
     * Actualiza el nombre visible del usuario en FirebaseAuth.
     */
    suspend fun updateDisplayName(name: String): Result<Unit> = runCatching {
        withTimeout(10_000) {
            authRemoteDataSource.updateDisplayName(name)
        }
    }

    /**
     * Fuerza la recarga del usuario autenticado actual desde Firebase.
     */
    suspend fun reloadCurrentUser(): Result<Unit> = runCatching {
        withTimeout(10_000) {
            authRemoteDataSource.reloadCurrentUser()
        }
    }
}
