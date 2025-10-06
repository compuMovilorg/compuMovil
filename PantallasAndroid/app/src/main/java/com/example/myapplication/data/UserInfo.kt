package com.example.myapplication.data

data class UserInfo(
    val id: Int,
    val username: String,
    val email: String,
    val name: String,
    val birthdate: String,
    val followersCount: Int,
    val followingCount: Int,
    val profileImage: String?
)
