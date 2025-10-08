package com.example.myapplication.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.AuthRepository
import com.example.myapplication.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val UserRepository: UserRepository
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
            val result = authRepository.register(state.email, state.password)

            if (result.isSuccess) {
                val uid = authRepository.currentUser?.uid
                    ?: return@launch onError("No se pudo obtener el UID después del registro")

                // Guarda el perfil básico en Firestore
                val saveResult = UserRepository.registerUser(
                    name = state.name,
                    username = state.usuario,
                    birthdate = state.fechaNacimiento,
                    userId = uid
                )

                if (saveResult.isSuccess) {
                    clearForm()
                    onSuccess()
                } else {
                    val msg = saveResult.exceptionOrNull()?.message ?: "Error guardando el perfil"
                    _uiState.update { it.copy(errorMessage = msg) }
                    onError(msg)
                }
            } else {
                val msg = result.exceptionOrNull()?.message ?: "Error al registrar usuario"
                _uiState.update { it.copy(errorMessage = msg) }
                onError(msg)
            }
        }
    }


    private fun clearForm() {
        _uiState.value = RegisterState()
    }
}
