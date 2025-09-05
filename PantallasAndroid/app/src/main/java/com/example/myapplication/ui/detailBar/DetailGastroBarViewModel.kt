package com.example.myapplication.ui.detailBar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.local.LocalGastroBarProvider


class DetailGastroBarViewModel(
    private val gastroBarId: Int
) : ViewModel() {
    var uiState = DetailGastroBarUiState(
        LocalGastroBarProvider.gastroBars.first()
    )
        private set

    init {
        val gastroBar = LocalGastroBarProvider.gastroBars.find { it.id == gastroBarId }
        if (gastroBar != null) {
            uiState = DetailGastroBarUiState(gastroBar)
        }
    }
}

class DetailGastroBarViewModelFactory(private val gastroBarId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailGastroBarViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailGastroBarViewModel(gastroBarId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}