package com.example.myapplication.ui.register

import android.util.Log
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

private const val TAG = "RegisterVM"

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository // <- nombre en minúscula
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterState())
    val uiState: StateFlow<RegisterState> = _uiState

    fun updateName(input: String) { _uiState.update { it.copy(name = input) } }
    fun updateUsuario(input: String) { _uiState.update { it.copy(usuario = input) } }
    fun updateFechaNacimiento(input: String) { _uiState.update { it.copy(fechaNacimiento = input) } }
    fun updateEmail(input: String) { _uiState.update { it.copy(email = input) } }
    fun updatePassword(input: String) { _uiState.update { it.copy(password = input) } }

    private fun isFormValid(): Boolean {
        val s = _uiState.value
        return s.name.isNotBlank() &&
                s.usuario.isNotBlank() &&
                s.fechaNacimiento.isNotBlank() &&
                s.email.contains("@") &&
                s.password.length >= 6
    }

    fun register(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val state = _uiState.value
        if (!isFormValid()) {
            val msg = "Formulario inválido"
            Log.w(TAG, msg)
            onError(msg)
            return
        }

        viewModelScope.launch {
            Log.i(TAG, "Registro: iniciando Auth con email=${state.email}")
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val authRes = authRepository.register(state.email, state.password)
            if (authRes.isFailure) {
                val msg = authRes.exceptionOrNull()?.message ?: "Error al registrar usuario"
                Log.e(TAG, "Registro: Auth falló -> $msg", authRes.exceptionOrNull())
                _uiState.update { it.copy(isLoading = false, errorMessage = msg) }
                onError(msg)
                return@launch
            }

            val uid = authRepository.currentUser?.uid
            if (uid.isNullOrBlank()) {
                val msg = "Auth ok pero sin UID de Firebase"
                Log.e(TAG, msg)
                _uiState.update { it.copy(isLoading = false, errorMessage = msg) }
                onError(msg)
                return@launch
            }

            Log.i(TAG, "Registro: Auth OK. UID=$uid. Guardando perfil en Firestore...")
            val saveRes = userRepository.registerUser(
                name = state.name,
                username = state.usuario,
                birthdate = state.fechaNacimiento,
                userId = uid
            )

            if (saveRes.isSuccess) {
                Log.i(TAG, "Registro: Perfil guardado en Firestore (users/$uid)")
                clearForm()
                _uiState.update { it.copy(isLoading = false) }
                onSuccess()
            } else {
                val err = saveRes.exceptionOrNull()
                val msg = err?.message ?: "Error guardando el perfil en Firestore"
                Log.e(TAG, "Registro: Falló guardado en Firestore -> $msg", err)
                _uiState.update { it.copy(isLoading = false, errorMessage = msg) }
                onError(msg)
            }
        }
    }

    private fun clearForm() {
        _uiState.value = RegisterState()
    }
}
