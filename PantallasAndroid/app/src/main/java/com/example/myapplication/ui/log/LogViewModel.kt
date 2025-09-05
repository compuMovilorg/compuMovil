package com.example.myapplication.ui.log

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
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRemoteDataSource
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
                onError(e.message ?: "Error desconocido")
            }
        }
    }

    private fun clearForm() {
        _uiState.value = LoginState()
    }
}
