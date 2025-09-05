package com.example.myapplication.ui.detailBar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.GastroBar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DetailGastroBarViewModel(
    gastroBar: GastroBar
) : ViewModel() {
    private val _uiState = MutableStateFlow(DetailGastroBarUiState(gastroBar))
    val uiState: StateFlow<DetailGastroBarUiState> = _uiState.asStateFlow()

}

class DetailGastroBarViewModelFactory(
    private val gastroBar: GastroBar
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailGastroBarViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailGastroBarViewModel(gastroBar) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}