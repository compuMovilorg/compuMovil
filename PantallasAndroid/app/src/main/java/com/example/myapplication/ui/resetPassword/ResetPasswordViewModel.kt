package com.example.myapplication.ui.resetPassword

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ResetPasswordViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ResetPasswordState())
    val email: StateFlow<String> get() = MutableStateFlow(_uiState.value.email)
    val uiState: StateFlow<ResetPasswordState> = _uiState

    private val _navigateToLogin = MutableStateFlow(false)
    val navigateToLogin: StateFlow<Boolean> = _navigateToLogin

    // Actualiza el email
    fun updateEmail(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun sendResetPassword() {
        val currentEmail = _uiState.value.email
        println("Enviando link de restablecimiento a $currentEmail")
    }

    fun onNavigateToLogin() {
        _navigateToLogin.value = true
    }

    fun onNavigated() {
        _navigateToLogin.value = false
    }
}