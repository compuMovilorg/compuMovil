package com.example.myapplication.ui.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.auth.CurrentUserProvider
import com.example.myapplication.data.repository.AuthRepository
import com.example.myapplication.data.repository.StorageRepository
import com.example.myapplication.data.repository.UserRepository
import com.example.myapplication.data.UserInfo
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
            profilePicUrl = ""
        )
    )
    val uiState: StateFlow<EditProfileState> = _uiState

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            try {
                val id = currentUserProvider.currentUserId()
                if (!id.isNullOrBlank()) {
                    // Intentamos cargar el usuario desde el backend
                    userRepository.getUserById(id).fold(
                        onSuccess = { user ->
                            applyUserToState(user)
                            Log.d(TAG, "Cargado perfil desde backend id=$id")
                        },
                        onFailure = {
                            // Si falla, fallback a auth (email/photo) y dejamos los demás campos vacíos
                            Log.w(TAG, "No se pudo cargar user backend id=$id: ${it.message}")
                            applyAuthFallback()
                        }
                    )
                } else {
                    // Sin id — usar datos de auth como fallback
                    applyAuthFallback()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error cargando perfil", e)
                applyAuthFallback()
            }
        }
    }

    private fun applyUserToState(user: UserInfo) {
        _uiState.update {
            it.copy(
                name = user.name ?: "",
                usuario = user.username ?: "",
                birthdate = user.birthdate ?: "",
                email = user.email ?: "",
                profilePicUrl = user.profileImage ?: ""
            )
        }
    }

    private fun applyAuthFallback() {
        val authUser = authRepository.currentUser
        _uiState.update {
            it.copy(
                email = authUser?.email ?: "",
                profilePicUrl = authUser?.photoUrl?.toString() ?: ""
            )
        }
    }

    fun updateName(input: String) {
        _uiState.update { it.copy(name = input) }
    }

    fun updateUsuario(input: String) {
        _uiState.update { it.copy(usuario = input) }
    }

    fun updateFechaNacimiento(input: String) {
        _uiState.update { it.copy(birthdate = input) }
    }

    fun updateEmail(input: String) {
        _uiState.update { it.copy(email = input) }
    }

    fun updateProfilePic(url: String) {
        _uiState.update { it.copy(profilePicUrl = url) }
    }

    /**
     * Guarda cambios en el backend (UserRepository). No cambia FirebaseAuth.email aquí;
     * si quieres sincronizar email con FirebaseAuth, hazlo explícitamente (requiere reauth).
     */
    fun saveProfile(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val state = _uiState.value

        if (state.email.isBlank() || !state.email.contains("@")) {
            onError("Correo inválido")
            return
        }

        viewModelScope.launch {
            try {
                val id = currentUserProvider.currentUserId()
                if (id.isNullOrBlank()) {
                    onError("Usuario no autenticado")
                    return@launch
                }

                // Construir objeto o DTO según tu UserRepository.updateUser
                val updatedUser = UserInfo(
                    id = id,
                    name = state.name,
                    username = state.usuario,
                    birthdate = state.birthdate,
                    email = state.email,
                    profileImage = state.profilePicUrl,
                    followersCount = state.followersCount,
                    followingCount = state.followingCount
                )

                userRepository.updateUser(updatedUser).fold(
                    onSuccess = {
                        Log.d(TAG, "User updated successfully in backend for id=$id")
                        onSuccess()
                    },
                    onFailure = { ex ->
                        Log.w(TAG, "Error updating user in backend: ${ex.message}")
                        onError(ex.message ?: "Error actualizando perfil")
                    }
                )

            } catch (e: Exception) {
                Log.e(TAG, "Error inesperado en saveProfile", e)
                onError(e.message ?: "Error inesperado")
            }
        }
    }

    /**
     * Sube imagen, actualiza backend y Firebase Auth (si quieres).
     * storageRepository.uploadProfileImg(uri) -> Result<String?> with URL
     */
    fun uploadImageToFirebase(uri: Uri) {
        viewModelScope.launch {
            try {
                // 0) Opcional: podrías notificar loading en el estado aquí

                // 1) Subir imagen al storage
                val uploadResult = storageRepository.uploadProfileImg(uri)
                if (!uploadResult.isSuccess) {
                    Log.w(TAG, "uploadProfileImg failed: ${uploadResult.exceptionOrNull()?.message}")
                    return@launch
                }

                val url = uploadResult.getOrNull().orEmpty()
                if (url.isBlank()) {
                    Log.w(TAG, "uploadProfileImg returned empty URL")
                    return@launch
                }

                // 2) Actualizar estado local
                _uiState.update { it.copy(profilePicUrl = url) }

                // 3) Actualizar imagen en backend (usuario)
                try {
                    val id = currentUserProvider.currentUserId()
                    if (!id.isNullOrBlank()) {
                        userRepository.updateProfileImage(id, url).fold(
                            onSuccess = { Log.d(TAG, "Profile image updated in backend for id=$id") },
                            onFailure = { ex -> Log.w(TAG, "Failed updating profile image in backend: ${ex.message}") }
                        )
                    } else {
                        Log.w(TAG, "Cannot update backend profile image: currentUserId is null/blank")
                    }
                } catch (e: Exception) {
                    Log.w(TAG, "Exception while updating backend profile image", e)
                }

                // 4) Actualizar FirebaseAuth profileImage (opcional)
                try {
                    val authResult = authRepository.updateProfileImage(url)
                    if (authResult.isSuccess) {
                        Log.d(TAG, "FirebaseAuth profile image updated")
                        // opcional: authRepository.reloadCurrentUser() si quieres reflejar cambios inmediatamente
                        // authRepository.reloadCurrentUser()
                    } else {
                        Log.w(TAG, "Failed updating FirebaseAuth profile image: ${authResult.exceptionOrNull()?.message}")
                    }
                } catch (e: Exception) {
                    Log.w(TAG, "Exception while updating FirebaseAuth profile image", e)
                }

            } catch (e: Exception) {
                Log.e(TAG, "Error al subir imagen", e)
            } finally {
                // 0) Opcional: quitar loading si lo manejas en estado
            }
        }
    }
}
