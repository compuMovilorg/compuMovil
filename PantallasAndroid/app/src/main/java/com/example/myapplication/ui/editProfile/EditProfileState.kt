package com.example.myapplication.ui.profile

data class EditProfileState(
    val name: String = "",
    val usuario: String = "",
    val fechaNacimiento: String = "",
    val email: String = "",
    val profilePicUrl: String? = ""
)
