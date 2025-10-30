package com.example.myapplication.ui.barReviews

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.ReviewInfo
import com.example.myapplication.data.repository.GastroBarRepository
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
    private val gastroBarRepository: GastroBarRepository,
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

        viewModelScope.launch {
            if (!id.isNullOrBlank()) {
                // Si ya tenemos un ID, lo usamos directamente
                currentGastroBarId = id
                _uiState.update { it.copy(gastroBarId = id, gastroBarName = nameArg) }
                loadReviewsByBar(id)
            } else if (!nameArg.isNullOrBlank()) {
                // Si tenemos nombre pero no ID, buscamos el GastroBar
                findGastroBarIdByName(nameArg)
            } else {
                Log.e("BarReviewsVM", "Ni gastroBarId ni gastroBarName especificados")
                _uiState.update { it.copy(errorMessage = "GastroBar no especificado", isLoading = false) }
            }
        }
    }

    /**
     * Busca el ID del GastroBar por nombre usando el repositorio.
     * Si lo encuentra, carga las reseñas asociadas.
     */
    private suspend fun findGastroBarIdByName(name: String) {
        Log.d("BarReviewsVM", "Buscando gastrobar por nombre: '$name'")

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        val result = gastroBarRepository.getGastroBares()
        result.fold(
            onSuccess = { gastrobares ->
                val found = gastrobares.find { it.name.equals(name, ignoreCase = true) }
                if (found != null) {
                    Log.d("BarReviewsVM", "Gastrobar encontrado: id=${found.id}, name=${found.name}")
                    currentGastroBarId = found.id
                    _uiState.update {
                        it.copy(gastroBarId = found.id, gastroBarName = found.name)
                    }
                    loadReviewsByBar(found.id)
                } else {
                    Log.w("BarReviewsVM", "No se encontró gastrobar con nombre='$name'")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "No se encontró el gastrobar '$name'"
                        )
                    }
                }
            },
            onFailure = { throwable ->
                Log.e("BarReviewsVM", "Error buscando gastrobares", throwable)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = throwable.message ?: "Error al obtener gastrobares"
                    )
                }
            }
        )
    }

    private fun loadReviewsByBar(gastroBarId: String) {
        val id = gastroBarId.trim()
        currentGastroBarId = id

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            Log.d("BarReviewsVM", "Solicitando reseñas para gastrobar id='$id'")

            val result = reviewRepository.getReviewsByGastroBar(id)
            result.fold(
                onSuccess = { reviews ->
                    if (reviews.isEmpty()) {
                        Log.w("BarReviewsVM", "No se encontraron reseñas para id='$id'")
                    } else {
                        reviews.forEach { r ->
                            Log.d("BarReviewsVM", "Review cargada: id=${r.id}, placeName=${r.placeName}, userId=${r.userId}")
                        }
                    }

                    _uiState.update {
                        it.copy(
                            reviews = reviews,
                            isLoading = false,
                            errorMessage = if (reviews.isEmpty()) "No se encontraron reseñas para este gastrobar" else null
                        )
                    }
                },
                onFailure = { throwable ->
                    Log.e("BarReviewsVM", "Error cargando reseñas para id='$id'", throwable)
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

    fun sendOrDeleteReviewLike(reviewId: String, userId: String) {
        viewModelScope.launch {
            val currentState = _uiState.value
            val review = currentState.reviews.find { it.id == reviewId }

            if (review == null) {
                Log.w("BarReviewsVM", "sendOrDeleteReviewLike: No se encontró review con id='$reviewId'")
                return@launch
            }

            val result = reviewRepository.sendOrDeleteReviewLike(reviewId, userId)
            if (result.isSuccess) {
                _uiState.update { state ->
                    val updatedReviews = state.reviews.map { r ->
                        if (r.id == reviewId) {
                            if (r.liked) {
                                r.copy(likes = r.likes - 1, liked = false)
                            } else {
                                r.copy(likes = r.likes + 1, liked = true)
                            }
                        } else r
                    }
                    state.copy(reviews = updatedReviews)
                }
            } else {
                Log.e("BarReviewsVM", "Error al enviar/eliminar like: ${result.exceptionOrNull()?.message}")
            }
        }
    }
}
