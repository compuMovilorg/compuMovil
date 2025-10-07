package com.example.myapplication.data.auth

interface CurrentUserProvider {
    suspend fun currentUserId(): Int?
}
