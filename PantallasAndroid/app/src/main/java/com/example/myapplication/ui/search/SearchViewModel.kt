package com.example.myapplication.ui.search

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.GastroBar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class SearchViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SearchState())
    val uiState: StateFlow<SearchState> = _uiState

    // Inicializar la lista de gastrobares
    fun setGastroBars(bars: List<GastroBar>) {
        _uiState.update { it.copy(gastroBars = bars) }
    }

    // Actualizar la query de búsqueda
    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    // Obtener la lista filtrada según la búsqueda
    val filteredBars: List<GastroBar>
        get() = if (_uiState.value.searchQuery.isBlank()) {
            _uiState.value.gastroBars
        } else {
            _uiState.value.gastroBars.filter {
                it.name.contains(_uiState.value.searchQuery, ignoreCase = true)
            }
        }
}
