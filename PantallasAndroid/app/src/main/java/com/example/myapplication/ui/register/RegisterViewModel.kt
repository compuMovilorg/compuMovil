// RegisterViewModel.kt
package com.example.myapplication.ui.register

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class RegisterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterState())
    val uiState: StateFlow<RegisterState> = _uiState

    fun updateName(input: String) {
        _uiState.update { it.copy(name = input) }
    }

    fun updateUsuario(input: String) {
        _uiState.update { it.copy(usuario = input) }
    }

    fun updateFechaNacimiento(input: String) {
        _uiState.update { it.copy(fechaNacimiento = input) }
    }

    fun updateEmail(input: String) {
        _uiState.update { it.copy(email = input) }
    }

    fun updatePassword(input: String) {
        _uiState.update { it.copy(password = input) }
    }

    fun register() {
        val state = _uiState.value
        val isValid = state.name.isNotBlank() &&
                state.usuario.isNotBlank() &&
                state.fechaNacimiento.isNotBlank() &&
                state.email.isNotBlank() &&
                state.password.isNotBlank()

        _uiState.update {
            if (isValid) {
                it.copy(result = RegisterResult.Success)
            } else {
                it.copy(result = RegisterResult.Error("Campos incompletos"))
            }
        }
    }

}
