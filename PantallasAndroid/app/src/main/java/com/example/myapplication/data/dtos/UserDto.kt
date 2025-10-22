package com.example.myapplication.data.dtos

import com.example.myapplication.data.UserInfo

abstract class UserDtoGeneric {
    abstract val id: String
    abstract val username: String
    abstract val email: String
    abstract val password: String
    abstract val name: String
    abstract val birthdate: String
    abstract val followersCount: Int
    abstract val followingCount: Int
    abstract val profileImage: String?

    abstract fun toUserInfo(): UserInfo
}


data class UserFirestoreDto(
    override val id: String,
    override val username: String,
    override val email: String,
    override val password: String,
    override val name: String,
    override val birthdate: String,
    override val followersCount: Int = 0,
    override val followingCount: Int = 0,
    override val profileImage: String?
) : UserDtoGeneric() {
    constructor() : this("", "", "", "", "", "", 0, 0, null)

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
    override val id: String,
    override val username: String,
    override val email: String,
    override val password: String,
    override val name: String,
    override val birthdate: String,
    override val followersCount: Int = 0,
    override val followingCount: Int = 0,
    override val profileImage: String?
) : UserDtoGeneric() {
    constructor() : this("", "", "", "", "", "", 0, 0, null)

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