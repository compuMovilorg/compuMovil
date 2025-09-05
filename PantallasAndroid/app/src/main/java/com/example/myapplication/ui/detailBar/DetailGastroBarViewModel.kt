package com.example.myapplication.ui.detailBar

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.GastroBar
import com.example.myapplication.data.local.LocalGastroBarProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DetailGastroBarViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(
        DetailGastroBarUiState()
    )
    val uiState: StateFlow<DetailGastroBarUiState> = _uiState

    fun buscarGastro(gastroBarId: Int) {
        val gastroBar = LocalGastroBarProvider.gastroBars.find { it.id == gastroBarId }
        _uiState.value = _uiState.value.copy(gastroBar = gastroBar)
    }
}
