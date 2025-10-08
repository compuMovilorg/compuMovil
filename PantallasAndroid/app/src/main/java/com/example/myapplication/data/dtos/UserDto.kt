package com.example.myapplication.data.dtos

import com.example.myapplication.data.UserInfo
import kotlin.toString

abstract class UserDtoGeneric{
    abstract fun toUserInfo(): UserInfo
}

data class UserFirestoreDto(
    val id: String,
    val username: String,
    val email: String,
    val password: String,
    val name: String,
    val birthdate: String,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val profileImage: String?
) : UserDtoGeneric() {
    constructor(): this("", "", "", "", "", "", 0, 0, null)

    override fun toUserInfo(): UserInfo {
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
    }

data class UserRetrofitDto(
    val id: String,
    val username: String,
    val email: String,
    val password: String,
    val name: String,
    val birthdate: String,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val profileImage: String?
) : UserDtoGeneric() {
    constructor(): this("", "", "", "", "", "", 0, 0, null)

    override fun toUserInfo(): UserInfo {
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
}