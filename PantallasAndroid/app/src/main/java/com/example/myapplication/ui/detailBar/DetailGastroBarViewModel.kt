package com.example.myapplication.ui.detailBar

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.GastroBar

class DetailGastroBarViewModel(
    gastroBar: GastroBar
) : ViewModel() {

    var uiState = DetailGastroBarUiState(gastroBar)
        private set

}
