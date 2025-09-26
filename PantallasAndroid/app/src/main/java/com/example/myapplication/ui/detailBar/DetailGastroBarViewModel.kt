package com.example.myapplication.ui.detailBar

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.local.LocalGastroBarProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DetailGastroBarViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(DetailGastroBarUiState())
    val uiState: StateFlow<DetailGastroBarUiState> = _uiState

    private val gastroBars = LocalGastroBarProvider.gastroBars

    init {
        _uiState.update { it.copy(gastroBar = gastroBars.firstOrNull()) }
    }

    fun buscarGastro(gastroBarId: Int) {
        val gastroBar = gastroBars.find { it.id == gastroBarId }
        _uiState.update { it.copy(gastroBar = gastroBar) }
    }
}
