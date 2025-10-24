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

    private var currentGastroBarId: String? = null

    init {
        val idRaw: String? = savedStateHandle["gastroBarId"]
        val nameArg: String? = savedStateHandle["gastroBarName"]
        val id = idRaw?.trim()

        Log.d("BarReviewsVM", "init: savedStateHandle gastroBarId='$idRaw' -> trimmed='$id', name='$nameArg'")

        if (id.isNullOrBlank()) {
            Log.e("BarReviewsVM", "Arg gastroBarId inválido o no especificado: '$idRaw'")
            _uiState.update { it.copy(errorMessage = "GastroBar no especificado", isLoading = false) }
        } else {
            currentGastroBarId = id
            _uiState.update { it.copy(gastroBarId = id, gastroBarName = nameArg) }
            loadReviewsByBar(id)
        }
    }

    fun setGastroBar(id: String, name: String? = null) {
        val trimmed = id.trim()
        currentGastroBarId = trimmed
        _uiState.update { it.copy(gastroBarId = trimmed, gastroBarName = name ?: it.gastroBarName) }
    }

    fun load(gastroBarId: String, gastroBarName: String? = null) {
        setGastroBar(gastroBarId, gastroBarName)
        loadReviewsByBar(gastroBarId)
    }

    /**
     * Llamada directa y única a getReviewsByGastroBar(id).
     * No hay fallback: si la query devuelve vacío o falla, el estado refleja ese resultado.
     */
    private fun loadReviewsByBar(gastroBarId: String) {
        val id = gastroBarId.trim()
        currentGastroBarId = id

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            Log.d("BarReviewsVM", "loadReviewsByBar -> solicitando reviews para id='$id'")

            try {
                val result = reviewRepository.getReviewsByGastroBar(id)
                result.fold(
                    onSuccess = { reviews ->
                        Log.d("BarReviewsVM", "getReviewsByGastroBar returned=${reviews.size} for id='$id'")
                        // Mantenemos exactamente lo que venga de la repo (sin intentar filtrar o adivinar)
                        _uiState.update {
                            it.copy(
                                reviews = reviews,
                                isLoading = false,
                                errorMessage = if (reviews.isEmpty()) "No se encontraron reseñas para este gastrobar" else null
                            )
                        }
                    },
                    onFailure = { throwable ->
                        Log.e("BarReviewsVM", "getReviewsByGastroBar FAILURE for id='$id': ${throwable.message}", throwable)
                        _uiState.update {
                            it.copy(
                                reviews = emptyList(),
                                isLoading = false,
                                errorMessage = throwable.message ?: "Error al cargar reseñas"
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                Log.e("BarReviewsVM", "Exception in loadReviewsByBar for id='$id': ${e.message}", e)
                _uiState.update {
                    it.copy(
                        reviews = emptyList(),
                        isLoading = false,
                        errorMessage = e.message ?: "Error interno al cargar reseñas"
                    )
                }
            }
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
    fun sendOrDeleteReviewLike(reviewId: String, userId: String) {
        viewModelScope.launch {
            val result = reviewRepository.sendOrDeleteReviewLike(reviewId, userId)
            if (result.isSuccess) {
                _uiState.update { state ->
                    state.copy(
                        reviews = state.reviews.map { review ->
                            if (review.id == reviewId) {
                                review.copy(likes = review.likes + 1)
                            } else review
                        }
                    )
                }
            }
        }
    }

}
