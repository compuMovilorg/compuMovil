package com.example.myapplication.data.auth

import android.util.Log
import com.example.myapplication.data.repository.AuthRepository
import com.example.myapplication.data.repository.UserRepository
import javax.inject.Inject

private const val TAG = "CurrentUserProvider"

class FirebaseCurrentUserProvider @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : CurrentUserProvider {
    override suspend fun currentUserId(): String? {
        TODO("Not yet implemented")
    }

    override suspend fun currentFirebaseUid(): String? {
        val uid = authRepository.currentUser?.uid
        Log.d(TAG, "currentFirebaseUid -> $uid")
        return uid
    }

    override suspend fun currentBackendUserId(): String? {
        val fbUser = authRepository.currentUser ?: return null
        val uid = fbUser.uid
        val email = fbUser.email

        // 1) Buscar por UID
        userRepository.getUserByFirebaseUid(uid).fold(
            onSuccess = { return it.id },
            onFailure = { Log.d(TAG, "No mapeado por uid=$uid: ${it.message}") }
        )

        // 2) Fallback por email
        if (!email.isNullOrBlank()) {
            userRepository.getUserByEmail(email).fold(
                onSuccess = { return it.id },
                onFailure = { Log.d(TAG, "No mapeado por email=$email: ${it.message}") }
            )
        }

        // 3) No reventar la app; simplemente no está mapeado
        Log.w(TAG, "Firebase autenticado pero sin mapping backend (uid=$uid, email=$email)")
        return null
    }
}
