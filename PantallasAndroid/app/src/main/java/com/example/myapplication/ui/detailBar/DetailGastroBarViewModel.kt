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
class DetailGastroBarViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val gastroBarId: Int = savedStateHandle["gastroBarId"] ?: -1

    private val _uiState = MutableStateFlow(
        DetailGastroBarUiState(
            gastroBar = LocalGastroBarProvider.gastroBars.find { it.id == gastroBarId }
        )
    )
    val uiState: StateFlow<DetailGastroBarUiState> = _uiState

    fun buscarGastro() {
        val gastroBar = LocalGastroBarProvider.gastroBars.find { it.id == gastroBarId }
        _uiState.value = _uiState.value.copy(gastroBar = gastroBar)
    }
}
