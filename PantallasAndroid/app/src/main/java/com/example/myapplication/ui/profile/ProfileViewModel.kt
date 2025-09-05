package com.example.myapplication.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileState())
    val uiState: StateFlow<ProfileState> = _uiState

    private val _events = MutableSharedFlow<ProfileEvent>()
    val events = _events.asSharedFlow()

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

    fun editProfile() {
        viewModelScope.launch { _events.emit(ProfileEvent.EditProfile) }
    }

    fun openHistory() {
        viewModelScope.launch { _events.emit(ProfileEvent.OpenHistory) }
    }

    fun openSaved() {
        viewModelScope.launch { _events.emit(ProfileEvent.OpenSaved) }
    }

    fun openNotifications() {
        viewModelScope.launch { _events.emit(ProfileEvent.OpenNotifications) }
    }

    fun openSettings() {
        viewModelScope.launch { _events.emit(ProfileEvent.OpenSettings) }
    }
}

sealed class ProfileEvent {
    object EditProfile : ProfileEvent()
    object OpenHistory : ProfileEvent()
    object OpenSaved : ProfileEvent()
    object OpenNotifications : ProfileEvent()
    object OpenSettings : ProfileEvent()
}