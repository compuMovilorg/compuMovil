package com.example.myapplication.ui.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.injection.BlockingDispatcher
import com.example.myapplication.data.injection.IoDispatcher
import com.example.myapplication.data.repository.AuthRepository
import com.example.myapplication.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuthException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "RegisterVM"

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    @BlockingDispatcher private val blockingDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterState())
    val uiState: StateFlow<RegisterState> = _uiState

    fun onRegisterSucces() {
        _uiState.update { it.copy(navigate = true, errorMessage = "") }
    }

    // --------- Setters ---------
    fun updateName(input: String) {
        Log.d(TAG, "updateName -> '$input'")
        _uiState.update { it.copy(name = input) }
    }

    fun updateUsername(input: String) {
        Log.d(TAG, "updateUsuario -> '$input'")
        _uiState.update { it.copy(username = input) }
    }

    fun updateBirthdate(input: String) {
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
        val username = s.username.trim()
        val birth = s.fechaNacimiento.trim()
        val email = s.email.trim()
        val pass = s.password

        if (name.isEmpty()) return "El nombre es obligatorio."
        if (username.isEmpty()) return "El usuario es obligatorio."
        if (birth.isEmpty()) return "La fecha de nacimiento es obligatoria."
        if (email.isEmpty()) return "El correo es obligatorio."
        if (pass.isEmpty()) return "La contraseña es obligatoria."
        if (!email.contains("@") || !email.contains(".")) return "Correo inválido."
        if (pass.length < 6) return "La contraseña debe tener al menos 6 caracteres."
        return null
    }

    // --------- Registro ---------
    fun register(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (_uiState.value.isLoading) {
            Log.d(TAG, "Registro ignorado: ya hay una operación en curso")
            return
        }

        logState("ANTES_DE_VALIDAR")

        validate()?.let { msg ->
            Log.d(TAG, "Formulario inválido -> $msg")
            _uiState.update { it.copy(errorMessage = msg, navigate = false) }
            onError(msg)
            return
        }

        val s = _uiState.value
        val name = s.name.trim()
        val username = s.username.trim()
        val birth = s.fechaNacimiento.trim()
        val email = s.email.trim()
        val pass = s.password

        // 🔸 Lanzamos en IO (o el que inyectes), pero las llamadas a Firebase/Firestore
        //     se envuelven con withContext(blockingDispatcher) para usar tiempo real.
        viewModelScope.launch(ioDispatcher) {
            try {
                _uiState.update { it.copy(isLoading = true, errorMessage = "", navigate = false) }

                Log.i(TAG, "Registro: iniciando Auth con email=$email")
                logState("ANTES_DE_AUTH_TRIMMED")

                // 🔸 Ejecuta Auth en tiempo real (evita timeout virtual)
                val authRes = withContext(blockingDispatcher) {
                    authRepository.register(email, pass)
                }

                Log.i(
                    TAG,
                    "DESPUES_DE_AUTH -> isSuccess=${authRes.isSuccess}, ex=${authRes.exceptionOrNull()?.javaClass?.simpleName}:${authRes.exceptionOrNull()?.message}"
                )

                if (authRes.isFailure) {
                    val ex = authRes.exceptionOrNull()
                    val friendly = firebaseFriendlyMessage(ex) ?: "Error al registrar usuario"
                    Log.e(TAG, "Auth falló: ${ex?.javaClass?.simpleName}: ${ex?.message}", ex)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = friendly,
                            navigate = false
                        )
                    }
                    withContext(Dispatchers.Main) { onError(friendly) }
                    return@launch
                }

                val uid = authRepository.currentUser?.uid
                if (uid.isNullOrBlank()) {
                    val msg = "Auth OK pero UID es nulo."
                    Log.e(TAG, msg)
                    _uiState.update { it.copy(isLoading = false, errorMessage = msg, navigate = false) }
                    withContext(Dispatchers.Main) { onError(msg) }
                    return@launch
                }

                Log.i(TAG, "Guardando perfil -> uid=$uid, name='$name', user='$username', birth='$birth'")
                logState("ANTES_DE_FIRESTORE_TRIMMED")

                // 🔸 Ejecuta Firestore en tiempo real (evita timeout virtual)
                val saveRes = withContext(blockingDispatcher) {
                    userRepository.registerUser(
                        name = name,
                        username = username,
                        birthdate = birth,
                        userId = uid,
                        FCMToken = ""
                    )
                }

                if (saveRes.isFailure) {
                    val ex = saveRes.exceptionOrNull()
                    val friendly = ex?.message ?: "Error guardando el perfil en Firestore"
                    Log.e(TAG, "Firestore falló: ${ex?.javaClass?.simpleName}: ${ex?.message}", ex)
                    _uiState.update { it.copy(isLoading = false, errorMessage = friendly, navigate = false) }
                    withContext(Dispatchers.Main) { onError(friendly) }   // ⬅️ Main
                    return@launch
                }

                Log.i(TAG, "Registro completo: perfil guardado en users/$uid")
                clearForm()
                _uiState.update { it.copy(isLoading = false, navigate = true, errorMessage = "") }
                withContext(Dispatchers.Main) {
                    onSuccess()
                }

            } catch (e: Exception) {
                val friendly = firebaseFriendlyMessage(e) ?: (e.message ?: "Error desconocido")
                Log.e(TAG, "Registro: excepción no controlada -> $friendly", e)
                _uiState.update { it.copy(isLoading = false, errorMessage = friendly, navigate = false) }
                onError(friendly)
            }
        }
    }

    private fun clearForm() {
        _uiState.value = RegisterState()
    }

    // --------- Mapeo de errores comunes de Firebase ---------
    private fun firebaseFriendlyMessage(e: Throwable?): String? {
        // 🔹 Normaliza el mensaje para detectar códigos genéricos sin depender de Firebase
        val rawMsg = e?.message.orEmpty()

        if (rawMsg.contains("EMAIL_ALREADY_IN_USE", ignoreCase = true) ||
            rawMsg.contains("already in use", ignoreCase = true) ||
            rawMsg.contains("USER_COLLISION", ignoreCase = true)) {
            return "Ya existe una cuenta con ese correo."
        }

        return when (e) {
            is com.google.firebase.auth.FirebaseAuthWeakPasswordException ->
                "La contraseña es demasiado débil."
            is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException ->
                "Correo o contraseña inválidos."
            is com.google.firebase.auth.FirebaseAuthUserCollisionException ->
                "Ya existe una cuenta con ese correo."
            is com.google.firebase.FirebaseNetworkException ->
                "Problema de conexión. Intenta de nuevo."
            is FirebaseAuthException -> when (e.errorCode) {
                "ERROR_EMAIL_ALREADY_IN_USE" -> "Ya existe una cuenta con ese correo."
                "ERROR_INVALID_EMAIL" -> "Correo inválido."
                "ERROR_WEAK_PASSWORD" -> "La contraseña es demasiado débil."
                else -> e.message
            }
            else -> e?.message
        }
    }


    private fun logState(label: String) {
        val s = _uiState.value
        Log.i(
            TAG,
            "$label | state = { " +
                    "name='${s.name}', user='${s.username}', birth='${s.fechaNacimiento}', " +
                    "email='${s.email}', passLen=${s.password.length}, isLoading=${s.isLoading} }"
        )
    }
}
