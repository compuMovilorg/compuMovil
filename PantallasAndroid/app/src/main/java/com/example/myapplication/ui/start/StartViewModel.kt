// StartViewModel.kt
package com.example.myapplication.ui.start

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class StartViewModel @Inject constructor() : ViewModel() {

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
