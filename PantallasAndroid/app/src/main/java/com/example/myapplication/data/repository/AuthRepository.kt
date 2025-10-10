package com.example.myapplication.data.repository

import com.example.myapplication.data.datasource.AuthRemoteDataSource
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource
) {
    val currentUser: FirebaseUser?
        get() = authRemoteDataSource.currentUser

    suspend fun login(email: String, password: String):Result<Unit>{
        try {
            authRemoteDataSource.login(email,password)
            return Result.success(Unit)
        }catch (e: Exception){
            return Result.failure(e)
        }
    }
    suspend fun register(email: String, password: String):Result<Unit>{
        try {
            authRemoteDataSource.register(email, password)
            return Result.success(Unit)
        }catch (e: Exception){
            return Result.failure(e)
        }
    }
    fun logout(){
        authRemoteDataSource.logout()
    }
}
