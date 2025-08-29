package com.example.myapplication.ui.resetPassword

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ResetPasswordViewModel : ViewModel() {

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    fun updateEmail(input: String) {
        _email.value = input
    }

    fun sendResetPassword() {
        // TODO: implementar logica de envio
        println("Enviando correo de restablecimiento a ${_email.value}")
    }
}
