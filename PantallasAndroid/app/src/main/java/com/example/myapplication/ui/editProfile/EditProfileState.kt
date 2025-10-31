package com.example.myapplication.ui.profile

data class EditProfileState(
    val name: String = "",
    val usuario: String = "",
    val birthdate: String = "",
    val email: String = "",
    val profilePicUrl: String = "",
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val saved: Boolean = false
)

