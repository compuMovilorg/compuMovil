package com.example.myapplication.ui.detailBar

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

    init {
        loadFirstGastroBar()
    }

    private fun loadFirstGastroBar() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = gastroBarRepository.getGastroBares()
            result.onSuccess { bars ->
                val firstBar = bars.firstOrNull()
                _uiState.update { it.copy(gastroBar = firstBar, isLoading = false) }
                firstBar?.name?.let { loadReviews(it) } // usamos name para filtrar reviews
            }.onFailure { throwable ->
                _uiState.update { it.copy(errorMessage = throwable.toString(), isLoading = false) }
            }
        }
    }

    fun buscarGastro(gastroBarId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = gastroBarRepository.getGastroBarById(gastroBarId)
            result.onSuccess { gastroBar ->
                _uiState.update { it.copy(gastroBar = gastroBar, isLoading = false) }
                gastroBar.name?.let { loadReviews(it) } // usamos name para filtrar reviews
            }.onFailure { throwable ->
                _uiState.update { it.copy(errorMessage = throwable.toString(), isLoading = false) }
            }
        }
    }

    private fun loadReviews(barName: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val reviewsResult = reviewRepository.getReviews()
                reviewsResult.onSuccess { allReviews ->

                    // Reviews del bar por nombre
                    val barReviews = allReviews.filter { it.placeName == barName }

                    // 🔧 Convertimos el id (String) a Int de forma segura para ambos usos
                    val reviewsWithReplies: List<Pair<Int, List<ReviewInfo>>> = barReviews.mapNotNull { review ->
                        val reviewIdInt = review.id.toIntOrNull()
                        if (reviewIdInt == null) {
                            // Si no se puede convertir, lo saltamos (o podrías manejarlo distinto)
                            null
                        } else {
                            val repliesResult = reviewRepository.getReviewsReplies(reviewIdInt)
                            val replies = repliesResult.getOrElse { emptyList() }
                            reviewIdInt to replies
                        }
                    }

                    _uiState.update {
                        it.copy(
                            reviews = barReviews,
                            reviewsReplies = reviewsWithReplies,
                            isLoading = false
                        )
                    }

                }.onFailure { throwable ->
                    _uiState.update { it.copy(errorMessage = throwable.toString(), isLoading = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.toString(), isLoading = false) }
            }
        }
    }

}
