package com.example.myapplication.data.dtos

import com.example.myapplication.data.UserInfo

data class UserDto(
    val id: Int,
    val username: String,
    val email: String,
    val password: String,
    val name: String,
    val birthdate: String,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val profileImage: String?
)

fun UserDto.toUserInfo(): UserInfo {
    return UserInfo(
        id = id,
        username = username,
        email = email,
        name = name,
        birthdate = birthdate,
        followersCount = followersCount,
        followingCount = followingCount,
        profileImage = profileImage
    )
}