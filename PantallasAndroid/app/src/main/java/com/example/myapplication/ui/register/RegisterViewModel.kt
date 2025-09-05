package com.example.myapplication.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.datasource.AuthRemoteDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRemoteDataSource
) : ViewModel() {

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

    private fun isFormValid(): Boolean {
        val state = _uiState.value
        return state.name.isNotBlank() &&
                state.usuario.isNotBlank() &&
                state.fechaNacimiento.isNotBlank() &&
                state.email.contains("@") &&
                state.password.length >= 6
    }

    fun register(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val state = _uiState.value
        if (!isFormValid()) {
            onError("Formulario inválido")
            return
        }

        viewModelScope.launch {
            try {
                authRepository.register(
                    email = state.email,
                    password = state.password
                )
                clearForm()
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Error desconocido")
            }
        }
    }

    private fun clearForm() {
        _uiState.value = RegisterState()
    }
}
