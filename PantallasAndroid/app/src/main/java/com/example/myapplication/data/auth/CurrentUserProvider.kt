package com.example.myapplication.data.auth

interface CurrentUserProvider {
    suspend fun currentUserId(): String?
    suspend fun currentFirebaseUid(): String?
//    suspend fun currentBackendUserId(): String?
}
