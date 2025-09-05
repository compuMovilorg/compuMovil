package com.example.myapplication.ui.log

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState

    private val _events = MutableStateFlow<LoginEvent?>(null)
    val events: StateFlow<LoginEvent?> = _events

    val email: StateFlow<String> get() = MutableStateFlow(_uiState.value.email)
    val password: StateFlow<String> get() = MutableStateFlow(_uiState.value.password)

    fun updateEmail(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun updatePassword(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }

    fun onLogin() {
        val current = _uiState.value
        if (current.email.isNotBlank() && current.password.isNotBlank()) {
            _events.value = LoginEvent.NavigateToHome
        } else {
            _events.value = LoginEvent.ShowError("Email y contraseña requeridos")
        }
    }

    fun onForgotPassword() {
        _events.value = LoginEvent.NavigateToForgotPassword
    }

    fun clearEvent() {
        _events.value = null
    }
}

sealed class LoginEvent {
    object NavigateToHome : LoginEvent()
    object NavigateToForgotPassword : LoginEvent()
    data class ShowError(val message: String) : LoginEvent()
}