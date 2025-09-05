package com.example.myapplication.ui.detailBar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.LocalGastroBarProvider
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch


class DetailGastroBarViewModel(
    private val gastroBarId: Int
) : ViewModel() {
    var uiState = DetailGastroBarUiState(
        LocalGastroBarProvider.gastroBars.first()
    )
        private set

    private val _events = MutableSharedFlow<DetailGastroBarEvent>()
    val events = _events.asSharedFlow()

    init {
        val gastroBar = LocalGastroBarProvider.gastroBars.find { it.id == gastroBarId }
        if (gastroBar != null) {
            uiState = DetailGastroBarUiState(gastroBar)
        }
    }

    fun onRateClick() {
        viewModelScope.launch { _events.emit(DetailGastroBarEvent.RateClick) }
    }

    fun onViewReviews() {
        viewModelScope.launch { _events.emit(DetailGastroBarEvent.ViewReviews) }
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

sealed class DetailGastroBarEvent {
    object RateClick : DetailGastroBarEvent()
    object ViewReviews : DetailGastroBarEvent()
}