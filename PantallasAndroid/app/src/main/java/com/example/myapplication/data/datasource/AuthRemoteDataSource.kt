package com.example.myapplication.data.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class AuthRemoteDataSource @Inject constructor(
    private val auth: FirebaseAuth
) {
    val currentUser: FirebaseUser?= auth.currentUser

    suspend fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()

    }

    suspend fun register(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).await()
    }

    fun logout() {
        auth.signOut()
    }
}
