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

    suspend fun login(email: String, password: String): Result<Unit> = runCatching {
        withTimeout(15_000) { authRemoteDataSource.login(email, password) }
        Unit
    }

    suspend fun register(email: String, password: String): Result<Unit> = runCatching {
        withTimeout(15_000) { authRemoteDataSource.register(email, password) }
        Unit
    }

    fun logout() = authRemoteDataSource.logout()

    /** Sign out con timeout y Result, alias de logout() */
    suspend fun signOut(): Result<Unit> = runCatching {
        withTimeout(5_000) { authRemoteDataSource.logout() }
        Unit
    }

    suspend fun updateProfileImage(url: String): Result<Unit> = runCatching {
        withTimeout(10_000) { authRemoteDataSource.updateProfileImage(url) }
        Unit
    }

    suspend fun updateDisplayName(name: String): Result<Unit> = runCatching {
        withTimeout(10_000) { authRemoteDataSource.updateDisplayName(name) }
        Unit
    }

    suspend fun reloadCurrentUser(): Result<Unit> = runCatching {
        withTimeout(10_000) { authRemoteDataSource.reloadCurrentUser() }
        Unit
    }

    suspend fun sendEmailVerification(): Result<Unit> = runCatching {
        withTimeout(15_000) { authRemoteDataSource.sendEmailVerification() }
        Unit
    }

    suspend fun reloadAndIsEmailVerified(): Result<Boolean> = runCatching {
        withTimeout(10_000) { authRemoteDataSource.reloadAndIsEmailVerified() }
    }

    val isEmailVerified: Boolean
        get() = authRemoteDataSource.isEmailVerified
}
