package com.example.myapplication.ui.profile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileState())
    val uiState: StateFlow<ProfileState> = _uiState

    // Actualiza el nombre
    fun updateUserName(input: String) {
        _uiState.update { it.copy(userName = input) }
    }

    // Actualiza el email
    fun updateUserEmail(input: String) {
        _uiState.update { it.copy(userEmail = input) }
    }

    // Actualiza la foto de perfil
    fun updateProfilePic(input: Int) {
        _uiState.update { it.copy(profilePic = input) }
    }
}
