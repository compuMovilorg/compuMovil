package com.example.myapplication.data.repository

import com.example.myapplication.data.datasource.AuthRemoteDataSource
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource
) {
    val currentUser: FirebaseUser? = authRemoteDataSource.currentUser

    suspend fun login(email: String, password: String){
        authRemoteDataSource.login(email, password)
    }
    suspend fun register(email: String, password: String){
        authRemoteDataSource.register(email, password)
    }
    fun logout(){
        authRemoteDataSource.logout()
    }
}
