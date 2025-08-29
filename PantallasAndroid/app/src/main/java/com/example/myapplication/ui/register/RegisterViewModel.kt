package com.example.myapplication.ui.register

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RegisterViewModel : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name
    fun updateName(input: String) {
        _name.value = input
    }

    private val _usuario = MutableStateFlow("")
    val usuario: StateFlow<String> = _usuario
    fun updateUsuario(input: String) {
        _usuario.value = input
    }

    private val _fechaNacimiento = MutableStateFlow("")
    val fechaNacimiento: StateFlow<String> = _fechaNacimiento
    fun updateFechaNacimiento(input: String) {
        _fechaNacimiento.value = input
    }

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email
    fun updateEmail(input: String) {
        _email.value = input
    }

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password
    fun updatePassword(input: String) {
        _password.value = input
    }
}
