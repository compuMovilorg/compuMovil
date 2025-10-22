package com.example.myapplication.ui.detailBar

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.ReviewInfo
import com.example.myapplication.data.repository.GastroBarRepository
import com.example.myapplication.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailGastroBarViewModel @Inject constructor(
    private val gastroBarRepository: GastroBarRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailGastroBarUiState())
    val uiState: StateFlow<DetailGastroBarUiState> = _uiState

    // ✅ Quitamos el init que cargaba un bar arbitrario
    // init { loadFirstGastroBar() }

    fun loadGastroBarAndReviews(gastroBarId: String) {
        viewModelScope.launch {
            Log.d("DetailVM", "Cargando información del gastrobar con id: $gastroBarId")
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = gastroBarRepository.getGastroBarById(gastroBarId)
            result.onSuccess { gastroBar ->
                Log.d("DetailVM", "Gastrobar obtenido: ${gastroBar.name}")
                _uiState.update { it.copy(gastroBar = gastroBar, isLoading = false) }

                gastroBar.name?.let {
                    Log.d("DetailVM", "Cargando reseñas del gastrobar: $it")
                    loadReviews(it)
                } ?: run {
                    Log.e("DetailVM", "El gastrobar no tiene nombre. No se pueden cargar reseñas.")
                }
            }.onFailure { throwable ->
                Log.e("DetailVM", "Error al obtener gastrobar: ${throwable.message}")
                _uiState.update { it.copy(errorMessage = throwable.toString(), isLoading = false) }
            }
        }
    }

    private fun loadReviews(barName: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            fun normalize(s: String?): String {
                return s?.trim()?.lowercase()?.replace(Regex("\\s+"), " ") ?: ""
            }

            val target = normalize(barName)
            Log.d("DetailVM", "Normalizando nombre de bar: '$barName' → '$target'")

            try {
                val reviewsResult = reviewRepository.getReviews()
                reviewsResult.onSuccess { allReviews ->
                    Log.d("DetailVM", "Total de reseñas obtenidas: ${allReviews.size}")

                    val barReviews = allReviews.filter { review ->
                        val reviewPlace = normalize(review.placeName)
                        reviewPlace == target ||
                                reviewPlace.contains(target) ||
                                (target.contains(reviewPlace) && reviewPlace.length >= 3)
                    }

                    Log.d("DetailVM", "Reseñas filtradas para '$barName': ${barReviews.size}")

                    val reviewsWithReplies = mutableListOf<Pair<String, List<ReviewInfo>>>()
                    for (review in barReviews) {
                        val reviewId = review.id ?: continue
                        try {
                            val repliesResult = reviewRepository.getReviewsReplies(reviewId)
                            val replies = repliesResult.getOrElse { emptyList() }
                            reviewsWithReplies.add(reviewId to replies)
                        } catch (e: Exception) {
                            Log.e("DetailVM", "Error al cargar respuestas de review $reviewId: ${e.message}")
                        }
                    }

                    _uiState.update {
                        it.copy(
                            reviews = barReviews,
                            reviewsReplies = reviewsWithReplies,
                            isLoading = false,
                            errorMessage = if (barReviews.isEmpty()) "No se encontraron reseñas para $barName" else null
                        )
                    }

                }.onFailure { throwable ->
                    Log.e("DetailVM", "Error al obtener reseñas: ${throwable.message}")
                    _uiState.update { it.copy(errorMessage = throwable.toString(), isLoading = false) }
                }
            } catch (e: Exception) {
                Log.e("DetailVM", "Excepción general al cargar reseñas: ${e.message}")
                _uiState.update { it.copy(errorMessage = e.toString(), isLoading = false) }
            }
        }
    }
}
