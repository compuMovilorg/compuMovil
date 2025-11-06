package com.example.myapplication.data.datasource

import android.net.Uri
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class AuthRemoteDataSource @Inject constructor(
    private val auth: FirebaseAuth
) {
    // ¡IMPORTANTE!: que sea un getter, no un val “congelado”
    val currentUser: FirebaseUser?
        get() = auth.currentUser

    suspend fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    /** Registra y devuelve el UID nuevo. */
    suspend fun register(email: String, password: String): String {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        return result.user?.uid ?: error("UID nulo después de crear usuario")
    }

    fun logout() {
        auth.signOut()
    }

    suspend fun updateProfileImage(photoUrl: String) {
        val uri = Uri.parse(photoUrl)
        val user = currentUser ?: error("No hay usuario autenticado")
        // Usamos userProfileChangeRequest builder por claridad
        val profileUpdates = userProfileChangeRequest {
            this.photoUri = uri
        }
        user.updateProfile(profileUpdates).await()
    }

    suspend fun updateDisplayName(name: String) {
        val user = currentUser ?: throw IllegalStateException("No user logged in")
        val profileUpdates = userProfileChangeRequest {
            displayName = name
        }
        user.updateProfile(profileUpdates).await()
    }

    suspend fun reloadCurrentUser() {
        auth.currentUser?.reload()?.await()
    }

    suspend fun sendEmailVerification(acs: ActionCodeSettings? = null) {
        val user = currentUser ?: error("No hay usuario autenticado para enviar verificación.")
        auth.setLanguageCode("es") // opcional
        if (acs != null) {
            user.sendEmailVerification(acs).await()
        } else {
            user.sendEmailVerification().await()
        }
    }

    /** Devuelve true si (tras recargar) el email está verificado. */
    suspend fun reloadAndIsEmailVerified(): Boolean {
        auth.currentUser?.reload()?.await()
        return currentUser?.isEmailVerified == true
    }

    /** Estado inmediato (sin recargar). Útil para UI reactiva. */
    val isEmailVerified: Boolean
        get() = currentUser?.isEmailVerified == true
}
