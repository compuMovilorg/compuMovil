package com.example.myapplication.ui.resetPassword

data class ResetPasswordState(
    val email: String = "",
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)
