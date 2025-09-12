package com.example.myapplication.data.datasource

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class AuthRemoteDataSource @Inject constructor(
    private val auth: FirebaseAuth
) {
    val currentUser: FirebaseUser?= auth.currentUser

    suspend fun login(email: String, password: String):Unit {
        auth.signInWithEmailAndPassword(email, password).await()

    }

    suspend fun register(email: String, password: String):Unit {
        auth.createUserWithEmailAndPassword(email, password).await()
    }

    fun logout() {
        auth.signOut()
    }

    suspend fun  updateProfileImage(photoUrl: String):Unit {
        val uri = Uri.parse(photoUrl)
        currentUser?.updateProfile(
            UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build()
        )?.await()
    }
}
