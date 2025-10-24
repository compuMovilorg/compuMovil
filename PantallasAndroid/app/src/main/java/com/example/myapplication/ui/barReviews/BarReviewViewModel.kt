package com.example.myapplication.ui.barReviews

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.ReviewInfo
import com.example.myapplication.data.repository.ReviewRepository
import com.example.myapplication.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BarReviewsViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(BarReviewsState())
    val uiState: StateFlow<BarReviewsState> = _uiState

    // ahora manejamos el id como String
    private var currentGastroBarId: String? = null

    init {
        // Leemos directamente como String desde los argumentos
        val idStr: String? = savedStateHandle["gastroBarId"]
        val nameArg: String? = savedStateHandle["gastroBarName"]

        if (idStr.isNullOrBlank()) {
            Log.e("BarReviewsVM", "Arg gastroBarId inválido o no especificado: $idStr")
            _uiState.update { it.copy(errorMessage = "GastroBar no especificado") }
        } else {
            // Actualiza el estado con el id como String
            _uiState.update { it.copy(gastroBarId = idStr, gastroBarName = nameArg) }
            loadReviewsByBar(idStr)
        }
    }

    fun setGastroBar(id: String, name: String? = null) {
        currentGastroBarId = id
        _uiState.update { it.copy(gastroBarId = id, gastroBarName = name ?: it.gastroBarName) }
    }

    fun load(gastroBarId: String, gastroBarName: String? = null) {
        if (gastroBarId.isBlank()) {
            Log.e(
                "BarReviewsVM",
                "load() recibió un gastroBarId vacío. No se puede cargar la lista de reseñas"
            )
            _uiState.update {
                it.copy(
                    gastroBarId = null,
                    gastroBarName = gastroBarName ?: it.gastroBarName,
                    isLoading = false,
                    errorMessage = "GastroBar no especificado"
                )
            }
            return
        }
        setGastroBar(gastroBarId, gastroBarName)
        loadReviewsByBar(gastroBarId)
    }

    private fun loadReviewsByBar(gastroBarId: String) {
        currentGastroBarId = gastroBarId
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = reviewRepository.getReviewsByGastroBar(gastroBarId)
            result.fold(
                onSuccess = { reviews ->
                    // Logs opcionales para depurar imágenes/URLs como en Home
                    reviews.forEach { r ->
                        Log.d("BarReviewsVM", "Review ID=${r.id}, placeImage=${r.placeImage}, userImage=${r.userImage}")
                    }

                    _uiState.update {
                        it.copy(
                            reviews = reviews,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { throwable ->
                    _uiState.update {
                        it.copy(
                            reviews = emptyList(),
                            isLoading = false,
                            errorMessage = throwable.message ?: "Error al cargar reseñas"
                        )
                    }
                }
            )
        }
    }

    fun reload() {
        currentGastroBarId?.let { loadReviewsByBar(it) }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    val filteredReviews: List<ReviewInfo>
        get() = if (_uiState.value.searchQuery.isBlank()) {
            _uiState.value.reviews
        } else {
            val q = _uiState.value.searchQuery
            _uiState.value.reviews.filter { review ->
                review.placeName.contains(q, ignoreCase = true) ||
                        review.reviewText.contains(q, ignoreCase = true)
            }
        }
}
