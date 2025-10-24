package com.example.myapplication.ui.user

import com.example.myapplication.data.ReviewInfo
import com.example.myapplication.data.UserInfo

data class UserState(
    val user: UserInfo? = null,
    val reviews: List<ReviewInfo> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userId: String? = null,
    val currentUserId: String? = null
)

