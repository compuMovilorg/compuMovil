package com.example.myapplication.ui.followingUsers

import com.example.myapplication.data.UserInfo

data class FollowingUsersState(
    val users: List<UserInfo> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
