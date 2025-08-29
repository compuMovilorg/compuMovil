package com.example.myapplication.ui.start

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StartViewModel : ViewModel() {

    private val _loginPressed = MutableStateFlow(false)
    val loginPressed: StateFlow<Boolean> = _loginPressed

    private val _registerPressed = MutableStateFlow(false)
    val registerPressed: StateFlow<Boolean> = _registerPressed

    fun onLoginPressed() {
        _loginPressed.value = true
    }

    fun onRegisterPressed() {
        _registerPressed.value = true
    }

    fun resetState() {
        _loginPressed.value = false
        _registerPressed.value = false
    }
}
