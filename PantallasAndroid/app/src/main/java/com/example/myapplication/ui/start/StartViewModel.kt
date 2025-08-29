// StartViewModel.kt
package com.example.myapplication.ui.start

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class StartViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(StartState())
    val uiState: StateFlow<StartState> = _uiState


    fun onLoginPressed() {
        _uiState.update { it.copy(loginPressed = true) }
    }

    fun onRegisterPressed() {
        _uiState.update { it.copy(registerPressed = true) }
    }

    fun resetState() {
        _uiState.update { it.copy(loginPressed = false, registerPressed = false) }
    }
}
