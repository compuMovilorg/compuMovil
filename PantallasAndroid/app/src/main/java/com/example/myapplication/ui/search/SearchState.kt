package com.example.myapplication.ui.search

import com.example.myapplication.data.GastroBar

data class SearchState(
    val searchQuery: String = "",
    val gastroBars: List<GastroBar> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)