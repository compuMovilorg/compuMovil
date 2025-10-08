package com.example.myapplication.ui.register

data class RegisterState(
    val name: String = "",
    val usuario: String = "",
    val fechaNacimiento: String = "",
    val email: String = "",
    val password: String = "",
    val navigate: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
