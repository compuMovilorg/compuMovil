package com.example.myapplication.ui.log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.datasource.AuthRemoteDataSource
import com.example.myapplication.data.repository.AuthRepository
import com.example.myapplication.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState

    fun updateEmail(input: String) {
        _uiState.update { it.copy(email = input) }
    }

    fun updatePassword(input: String) {
        _uiState.update { it.copy(password = input) }
    }

    private fun isFormValid(): Boolean {
        val state = _uiState.value
        return state.email.contains("@") && state.password.length >= 6
    }

    fun login(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val state = _uiState.value
        if (!isFormValid()) {
            onError("Formulario inválido")
            return
        }

        viewModelScope.launch {
            try {
                authRepository.login(
                    email = state.email,
                    password = state.password
                )
                clearForm()
                onSuccess()
            } catch (e: Exception) {
                val message = when (e) {
                    is FirebaseAuthInvalidUserException -> "El usuario no existe o fue eliminado."
                    is FirebaseAuthInvalidCredentialsException -> "Correo o contraseña incorrectos."
                    is FirebaseAuthRecentLoginRequiredException -> "La sesión expiró, inicia sesión de nuevo."
                    else -> e.message ?: "Error desconocido"
                }
                onError(message)
            }
        }
    }

    private fun clearForm() {
        _uiState.value = LoginState()
    }
}
