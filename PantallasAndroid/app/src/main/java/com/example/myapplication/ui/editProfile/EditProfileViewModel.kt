package com.example.myapplication.ui.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.datasource.AuthRemoteDataSource
import com.example.myapplication.data.datasource.StorageRemoteDataSource
import com.example.myapplication.data.repository.StorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val authRepository: AuthRemoteDataSource,
    private val storageRepository: StorageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileState(
        email = authRepository.currentUser?.email ?:"",
        profilePicUrl = authRepository .currentUser?.photoUrl?.toString() ?: ""
    ))
    val uiState: StateFlow<EditProfileState> = _uiState

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


//    fun updateProfilePic(url: String) {
//        _uiState.update { it.copy(profilePicUrl = url) }
//    }

    fun saveProfile(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val state = _uiState.value

        if (state.email.isBlank() || !state.email.contains("@")) {
            onError("Correo inválido")
            return
        }
    }

    fun uploadImageToFirebase(uri: Uri){
        viewModelScope.launch {
            val result = storageRepository.uploadProfileImg(uri)
            if (result.isSuccess){
                _uiState.update { it.copy(profilePicUrl = result.getOrNull()) }

                authRepository.updateProfileImage(result.getOrNull() ?: "")
            }
        }
    }
}
