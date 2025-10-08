package com.example.myapplication.data.auth

import android.util.Log
import com.example.myapplication.data.repository.AuthRepository
import com.example.myapplication.data.repository.UserRepository
import javax.inject.Inject

class FirebaseCurrentUserProvider @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : CurrentUserProvider {

    override suspend fun currentUserId(): Int? {
        val fbUser = authRepository.currentUser ?: return null

        val uid = fbUser.uid
        val email = fbUser.email

        // 1) Intentar por UID
        userRepository.getUserByFirebaseUid(uid).fold(
            onSuccess = { return it.id.toInt() },
            onFailure = { Log.d("CurrentUserProvider", "No mapeado por uid=$uid") }
        )

        // 2) Intentar por email (si existe)
        if (!email.isNullOrBlank()) {
            userRepository.getUserByEmail(email).fold(
                onSuccess = { return it.id.toInt() },
                onFailure = { Log.d("CurrentUserProvider", "No mapeado por email=$email") }
            )
        }

        // 3) No crear ni registrar: falla explícita
        throw IllegalStateException(
            "Usuario autenticado en Firebase pero no existe en el backend (uid=$uid, email=$email)"
        )
    }
}
