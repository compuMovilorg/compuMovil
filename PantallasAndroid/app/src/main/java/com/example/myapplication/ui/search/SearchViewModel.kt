package com.example.myapplication.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.GastroBar
import com.example.myapplication.data.repository.GastroBarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "SearchViewModel"
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val gastroBarRepository: GastroBarRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchState())
    val uiState: StateFlow<SearchState> = _uiState

    init {
        loadGastroBares()
    }

    /** Carga remota de gastrobares (una vez al iniciar o cuando se refresque). */
    fun loadGastroBares() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = gastroBarRepository.getGastroBares()
            result.fold(
                onSuccess = { bars ->
                    if (bars.isEmpty()) {
                        Log.w(TAG, "getGastroBares() devolvió una lista vacía")
                    } else {
                        bars.forEach { b ->
                            Log.d(TAG, "GastroBar cargado: id=${b.id}, name=${b.name}")
                        }
                    }
                    _uiState.update {
                        it.copy(
                            gastroBars = bars,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { throwable ->
                    Log.e(TAG, "Error al cargar gastrobares", throwable)
                    _uiState.update {
                        it.copy(
                            gastroBars = emptyList(),
                            isLoading = false,
                            errorMessage = throwable.message ?: "Error al cargar gastrobares"
                        )
                    }
                }
            )
        }
    }

    /** Para pull-to-refresh o reintento manual. */
    fun refresh() = loadGastroBares()

    /** Actualiza la query de búsqueda (usada por el UI). */
    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    /** Lista filtrada según la búsqueda actual. */
    val filteredBars: List<GastroBar>
        get() = if (_uiState.value.searchQuery.isBlank()) {
            _uiState.value.gastroBars
        } else {
            val q = _uiState.value.searchQuery
            _uiState.value.gastroBars.filter { bar ->
                bar.name.contains(q, ignoreCase = true)
            }
        }
}
