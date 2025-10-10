package com.example.myapplication.ui.profile
import com.example.myapplication.data.ReviewInfo

data class ProfileState(
    val userId: Int? = null,
    val userName: String = "",
    val userEmail: String = "",
    val profilePicUrl: String? = "",
    val reviews: List<ReviewInfo> = emptyList(),
    val isLoadingReviews: Boolean = false,
    val reviewsError: String? = null
)
