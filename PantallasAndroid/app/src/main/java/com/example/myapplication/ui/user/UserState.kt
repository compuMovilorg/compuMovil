package com.example.myapplication.ui.user

import com.example.myapplication.data.ReviewInfo
import com.example.myapplication.data.UserInfo

data class UserState(
    var user: UserInfo? = null,
    var reviews: List<ReviewInfo> = emptyList(),
    var isLoading: Boolean = false,
    var errorMessage: String? = null
)
