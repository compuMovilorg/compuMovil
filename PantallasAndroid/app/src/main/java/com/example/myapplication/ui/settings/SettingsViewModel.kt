// SettingsViewModel.kt
package com.example.myapplication.ui.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsState())
    val uiState: StateFlow<SettingsState> = _uiState


    fun logout() {
        _uiState.update { it.copy(isLoggedOut = true) }
    }
}
