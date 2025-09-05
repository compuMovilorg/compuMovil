package com.example.myapplication.ui.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileState())
    val uiState: StateFlow<ProfileState> = _uiState

    fun updateUserName(input: String) {
        _uiState.update { it.copy(userName = input) }
    }

    fun updateUserEmail(input: String) {
        _uiState.update { it.copy(userEmail = input) }
    }

    fun updateProfilePic(input: Int) {
        _uiState.update { it.copy(profilePic = input) }
    }
}
