package com.example.myapplication.ui.search

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.GastroBar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

class SearchViewModel(
    private val gastroBars: List<GastroBar>
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val filteredBars: StateFlow<List<GastroBar>> =
        _searchQuery.map { query ->
            if (query.isBlank()) gastroBars
            else gastroBars.filter { it.name.contains(query, ignoreCase = true) }
        } as StateFlow<List<GastroBar>>

    fun updateQuery(newQuery: String) {
        _searchQuery.value = newQuery
    }
}
