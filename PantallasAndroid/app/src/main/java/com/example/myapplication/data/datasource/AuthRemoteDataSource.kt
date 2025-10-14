package com.example.myapplication.data.datasource

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
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
        // usa SIEMPRE el getter dinámico
        val user = currentUser ?: error("No hay usuario autenticado")
        user.updateProfile(
            UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build()
        ).await()
    }
}
