package com.example.myapplication.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.GastroBar

class SearchViewModelFactory(
    private val gastroBars: List<GastroBar>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(gastroBars) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}