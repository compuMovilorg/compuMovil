package com.example.myapplication.ui.profile

data class EditProfileState(
    val name: String = "",
    val usuario: String = "",
    val birthdate: String = "",
    val email: String = "",
    val profilePicUrl: String? = null,
    val followersCount: Int = 0,
    val followingCount: Int = 0,

    // flags para la UI
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
