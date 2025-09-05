package com.example.myapplication.ui.register

data class RegisterState(
    val name: String = "",
    val usuario: String = "",
    val fechaNacimiento: String = "",
    val email: String = "",
    val password: String = "",
    val result: RegisterResult? = null
)

sealed class RegisterResult {
    object Success : RegisterResult()
    data class Error(val message: String) : RegisterResult()
}