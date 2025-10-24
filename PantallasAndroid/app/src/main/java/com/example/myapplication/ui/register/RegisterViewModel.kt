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
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterState())
    val uiState: StateFlow<RegisterState> = _uiState

    // --------- Setters (con trim + logs) ---------
    fun updateName(input: String) {
        Log.d(TAG, "updateName -> '$input'")
        _uiState.update { it.copy(name = input) }
    }
    fun updateUsuario(input: String) {
        Log.d(TAG, "updateUsuario -> '$input'")
        _uiState.update { it.copy(usuario = input) }
    }
    fun updateFechaNacimiento(input: String) {
        Log.d(TAG, "updateFechaNacimiento -> '$input'")
        _uiState.update { it.copy(fechaNacimiento = input) }
    }
    fun updateEmail(input: String) {
        _uiState.update { it.copy(email = input) }
    }
    fun updatePassword(input: String) {
        _uiState.update { it.copy(password = input) }
    }

    // --------- Validación ---------
    private fun validate(): String? {
        val s = _uiState.value
        val name = s.name.trim()
        val user = s.usuario.trim()
        val birth = s.fechaNacimiento.trim()
        val email = s.email.trim()
        val pass = s.password

        if (name.isEmpty()) return "El nombre es obligatorio."
        if (user.isEmpty()) return "El usuario es obligatorio."
        if (birth.isEmpty()) return "La fecha de nacimiento es obligatoria."
        if (!email.contains("@") || !email.contains(".")) return "Correo inválido."
        if (pass.length < 6) return "La contraseña debe tener al menos 6 caracteres."
        return null
    }

    // --------- Registro ---------
    fun register(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (_uiState.value.isLoading) {
            Log.w(TAG, "Registro ignorado: ya hay una operación en curso")
            return
        }

        // 1) Antes de validar (lo que hay escrito en el formulario)
        logState("ANTES_DE_VALIDAR")

        validate()?.let { msg ->
            Log.w(TAG, "Formulario inválido -> $msg")
            _uiState.update { it.copy(errorMessage = msg) }
            onError(msg)
            return
        }

        // Snapshot con trim
        val s = _uiState.value
        val name = s.name.trim()
        val user = s.usuario.trim()
        val birth = s.fechaNacimiento.trim()
        val email = s.email.trim()
        val pass = s.password

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }

                // 2) Justo antes de Auth, con snapshot trimmed (lo que realmente se usará)
                Log.i(TAG, "Registro: iniciando Auth con email=$email")
                logState("ANTES_DE_AUTH_TRIMMED")

                val authRes = authRepository.register(email, pass)

                // 3) Apenas retorna Auth (éxito o error)
                Log.i(
                    TAG,
                    "DESPUES_DE_AUTH -> isSuccess=${authRes.isSuccess}, ex=${authRes.exceptionOrNull()?.javaClass?.simpleName}:${authRes.exceptionOrNull()?.message}"
                )

                if (authRes.isFailure) {
                    val ex = authRes.exceptionOrNull()
                    val friendly = firebaseFriendlyMessage(ex) ?: "Error al registrar usuario"
                    Log.e(TAG, "Auth falló: ${ex?.javaClass?.simpleName}: ${ex?.message}", ex)
                    _uiState.update { it.copy(isLoading = false, errorMessage = friendly) }
                    onError(friendly)
                    return@launch
                }

                val uid = authRepository.currentUser?.uid
                if (uid.isNullOrBlank()) {
                    val msg = "Auth OK pero UID es nulo."
                    Log.e(TAG, msg)
                    _uiState.update { it.copy(isLoading = false, errorMessage = msg) }
                    onError(msg)
                    return@launch
                }

                // 4) Justo antes de enviar a Firestore (valores finales que se guardarán)
                Log.i(TAG, "Guardando perfil -> uid=$uid, name='$name', user='$user', birth='$birth'")
                logState("ANTES_DE_FIRESTORE_TRIMMED")

                val saveRes = userRepository.registerUser(
                    name = name,
                    username = user,
                    birthdate = birth,
                    userId = uid,
                    FCMToken = ""
                )

                if (saveRes.isFailure) {
                    val ex = saveRes.exceptionOrNull()
                    val friendly = ex?.message ?: "Error guardando el perfil en Firestore"
                    Log.e(TAG, "Firestore falló: ${ex?.javaClass?.simpleName}: ${ex?.message}", ex)
                    _uiState.update { it.copy(isLoading = false, errorMessage = friendly) }
                    onError(friendly)
                    return@launch
                }

                Log.i(TAG, "Registro completo: perfil guardado en users/$uid")
                clearForm()
                _uiState.update { it.copy(isLoading = false) }
                onSuccess()

            } catch (e: Exception) {
                val friendly = firebaseFriendlyMessage(e) ?: (e.message ?: "Error desconocido")
                Log.e(TAG, "Registro: excepción no controlada -> $friendly", e)
                _uiState.update { it.copy(isLoading = false, errorMessage = friendly) }
                onError(friendly)
            }
        }
    }


    private fun clearForm() {
        _uiState.value = RegisterState()
    }

    // --------- Mapeo de errores comunes de Firebase ---------
    private fun firebaseFriendlyMessage(e: Throwable?): String? {
        return when (e) {
            is com.google.firebase.auth.FirebaseAuthWeakPasswordException ->
                "La contraseña es demasiado débil."
            is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException ->
                "Correo o contraseña inválidos."
            is com.google.firebase.auth.FirebaseAuthUserCollisionException ->
                "Ya existe una cuenta con ese correo."
            is com.google.firebase.FirebaseNetworkException ->
                "Problema de conexión. Intenta de nuevo."
            else -> e?.message
        }
    }

    private fun logState(label: String) {
        val s = _uiState.value
        Log.i(
            TAG,
            "$label | state = { " +
                    "name='${s.name}', user='${s.usuario}', birth='${s.fechaNacimiento}', " +
                    "email='${s.email}', passLen=${s.password.length}, isLoading=${s.isLoading} }"
        )
    }

}
