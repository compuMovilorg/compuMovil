package com.example.myapplication.data.dtos

import com.example.myapplication.data.UserInfo
import com.google.firebase.firestore.DocumentSnapshot

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

    abstract val followed: Boolean

    abstract fun toUserInfo(): UserInfo
}


// 4) DTO para actualizar perfil (solo campos editables)
data class UpdateUserDto(
    val username: String? = null,
    val name: String? = null,
    val birthdate: String? = null,
    val email: String? = null,
    val profileImage: String? = null
)

data class UserFirestoreDto(
    override val id: String,
    override val username: String,
    override val email: String,
    override val password: String,
    override val name: String,
    override val birthdate: String,
    override val followersCount: Int = 0,
    override val followingCount: Int = 0,
    override val profileImage: String?,
    override var followed: Boolean = false
) : UserDtoGeneric() {
    constructor() : this("", "", "", "", "", "", 0, 0, null, false)

    override fun toUserInfo(): UserInfo {
        return UserInfo(
            id = id,
            username = username,
            email = email,
            name = name,
            birthdate = birthdate,
            followersCount = followersCount,
            followingCount = followingCount,
            profileImage = profileImage ?: "No hay foto de perfil",
            followed = followed
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
    override val profileImage: String?,
    override val followed: Boolean = false
) : UserDtoGeneric() {
    constructor() : this("", "", "", "", "", "", 0, 0, null, false)

    override fun toUserInfo(): UserInfo {
        return UserInfo(
            id = id,
            username = username,
            email = email,
            name = name,
            birthdate = birthdate,
            followersCount = followersCount,
            followingCount = followingCount,
            profileImage = profileImage ?: "No hay foto de perfil",
            followed = followed
        )
    }
}