package com.example.myapplication.ui.resetPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ResetPasswordViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ResetPasswordState())
    val uiState: StateFlow<ResetPasswordState> = _uiState

    fun updateEmail(newEmail: String) {
        _uiState.update { it.copy(email = newEmail, errorMessage = null, successMessage = null) }
    }

    /**
     * Lógica real de envío de restablecimiento de contraseña
     * Aquí deberías integrar FirebaseAuth o tu backend.
     */
    fun sendResetPassword() {
        val currentEmail = _uiState.value.email

        if (currentEmail.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El correo no puede estar vacío") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }

            try {
                // 🔹 Aquí llamas tu lógica real (ejemplo con Firebase)
                // FirebaseAuth.getInstance().sendPasswordResetEmail(currentEmail).await()

                // Cuando sea exitoso:
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        successMessage = "Se envió el link de restablecimiento a $currentEmail"
                    )
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error al enviar el correo"
                    )
                }
            }
        }
    }
}
