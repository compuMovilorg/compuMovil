package com.example.myapplication.data.dtos

import com.example.myapplication.data.UserInfo

data class UserProfileDto(
    val id: Int? = null,
    val username: String,
    val email: String,
    val password: String,
    val name: String,
    val birthdate: String,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val profileImage: String? = null
)

fun UserProfileDto.toUserInfo(): UserInfo {
    return UserInfo(
        id = id ?: 0,
        username = username,
        email = email,
        name = name,
        birthdate = birthdate,
        followersCount = followersCount,
        followingCount = followingCount,
        profileImage = profileImage
    )
}