package com.example.myapplication.ui.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.UserInfo
import com.example.myapplication.data.auth.CurrentUserProvider
import com.example.myapplication.data.repository.AuthRepository
import com.example.myapplication.data.repository.StorageRepository
import com.example.myapplication.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "EditProfileVM"

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val currentUserProvider: CurrentUserProvider,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val storageRepository: StorageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        EditProfileState(
            email = "",
            profilePicUrl = "",
            isLoading = false,
            errorMessage = null,
            successMessage = null,
            saved = false
        )
    )
    val uiState: StateFlow<EditProfileState> = _uiState

    init { loadProfile() }

    /** Public para poder re-invocarlo desde la UI si hace falta. */
    fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null, saved = false) }
            try {
                val id = currentUserProvider.currentUserId()
                if (id.isNullOrBlank()) {
                    applyAuthFallback()
                } else {
                    userRepository.getUserById(id).fold(
                        onSuccess = { user ->
                            applyUserToState(user)
                            Log.d(TAG, "Perfil cargado (id=$id)")
                        },
                        onFailure = { ex ->
                            Log.w(TAG, "Falló cargar user id=$id: ${ex.message}")
                            applyAuthFallback()
                        }
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error cargando perfil", e)
                applyAuthFallback()
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun applyUserToState(user: UserInfo) {
        _uiState.update {
            it.copy(
                name = user.name?.trim().orEmpty(),
                usuario = user.username?.trim().orEmpty(),
                birthdate = user.birthdate?.trim().orEmpty(),
                email = user.email?.trim().orEmpty(),
                profilePicUrl = user.profileImage?.trim().orEmpty(),
                followersCount = user.followersCount,
                followingCount = user.followingCount,
                errorMessage = null
            )
        }
    }

    private fun applyAuthFallback() {
        val authUser = authRepository.currentUser
        _uiState.update {
            it.copy(
                email = authUser?.email?.trim().orEmpty(),
                profilePicUrl = authUser?.photoUrl?.toString().orEmpty(),
                errorMessage = null
            )
        }
    }

    fun updateName(input: String)     { _uiState.update { it.copy(name = input) } }
    fun updateUsuario(input: String)  { _uiState.update { it.copy(usuario = input) } }
    fun updateFechaNacimiento(input: String) { _uiState.update { it.copy(birthdate = input) } }
    fun updateEmail(input: String)    { _uiState.update { it.copy(email = input) } }
    fun updateProfilePic(url: String) { _uiState.update { it.copy(profilePicUrl = url) } }

    fun acknowledgeSaved() {
        _uiState.update { it.copy(saved = false, successMessage = null) }
    }
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun saveProfile(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val s = _uiState.value
        val name = s.name.trim()
        val username = s.usuario.trim()
        val birth = s.birthdate.trim()
        val email = s.email.trim()

        // Validación básica
        val err = when {
            name.isEmpty() -> "El nombre es obligatorio."
            username.isEmpty() -> "El usuario es obligatorio."
            email.isEmpty() || !email.contains("@") || !email.contains(".") -> "Correo inválido."
            else -> null
        }
        if (err != null) {
            _uiState.update { it.copy(errorMessage = err, successMessage = null) }
            onError(err)
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null, saved = false) }
            try {
                val id = currentUserProvider.currentUserId()
                if (id.isNullOrBlank()) {
                    val msg = "Usuario no autenticado"
                    _uiState.update { it.copy(errorMessage = msg, isLoading = false) }
                    onError(msg); return@launch
                }

                val updatedUser = UserInfo(
                    id = id,
                    name = name,
                    username = username,
                    birthdate = birth,
                    email = email,
                    profileImage = s.profilePicUrl,
                    followersCount = s.followersCount,
                    followingCount = s.followingCount
                )

                userRepository.updateUser(updatedUser).fold(
                    onSuccess = {
                        // Actualiza estado local para reflejar lo guardado
                        _uiState.update {
                            it.copy(
                                name = name,
                                usuario = username,
                                birthdate = birth,
                                email = email,
                                successMessage = "Perfil actualizado",
                                errorMessage = null,
                                saved = true
                            )
                        }
                        onSuccess()
                    },
                    onFailure = { ex ->
                        val msg = ex.message ?: "Error actualizando perfil"
                        _uiState.update { it.copy(errorMessage = msg) }
                        onError(msg)
                    }
                )
            } catch (e: Exception) {
                val msg = e.message ?: "Error inesperado"
                _uiState.update { it.copy(errorMessage = msg) }
                onError(msg)
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun uploadImageToFirebase(uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            try {
                val uploadResult = storageRepository.uploadProfileImg(uri)
                if (!uploadResult.isSuccess) {
                    val msg = uploadResult.exceptionOrNull()?.message ?: "Error subiendo imagen"
                    _uiState.update { it.copy(errorMessage = msg) }
                    return@launch
                }

                val url = uploadResult.getOrNull().orEmpty()
                if (url.isBlank()) {
                    _uiState.update { it.copy(errorMessage = "URL de imagen vacía") }
                    return@launch
                }

                _uiState.update { it.copy(profilePicUrl = url) }

                // Backend
                val id = currentUserProvider.currentUserId()
                if (!id.isNullOrBlank()) {
                    userRepository.updateProfileImage(id, url).onFailure { ex ->
                        Log.w(TAG, "Backend img update failed: ${ex.message}")
                    }
                }

                // FirebaseAuth (opcional)
                authRepository.updateProfileImage(url).onFailure { ex ->
                    Log.w(TAG, "Auth img update failed: ${ex.message}")
                }

                _uiState.update { it.copy(successMessage = "Imagen actualizada", errorMessage = null) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Error al subir imagen") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
