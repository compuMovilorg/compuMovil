package com.example.myapplication.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import com.example.myapplication.data.ReviewInfo
import com.example.myapplication.data.repository.GastroBarRepository
import com.example.myapplication.data.repository.ReviewRepository
import com.example.myapplication.data.repository.StorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val gastroBarRepository: GastroBarRepository,
    private val reviewRepository: ReviewRepository,
    private val storageRepository: StorageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState

    init {
        loadReviews()
        loadGastrobares()
    }

    private fun loadReviews() {
//        viewModelScope.launch {
//            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
//
//            try {
//                reviewRepository.getReviewsLive()
//                    .collect { reviews ->
//                        // Logs para depuración
//                        if (reviews.isEmpty()) {
//                            Log.w("HomeViewModel", "getReviewsLive() devolvió una lista vacía")
//                        } else {
//                            reviews.forEach { r ->
//                                Log.d(
//                                    "HomeViewModel",
//                                    "Review cargada: id=${r.id}, placeName=${r.placeName}, userId=${r.userId}"
//                                )
//                            }
//                        }
//
//                        _uiState.update {
//                            it.copy(
//                                reviews = reviews,
//                                isLoading = false,
//                                errorMessage = null
//                            )
//                        }
//                    }
//            } catch (throwable: Throwable) {
//                Log.e("HomeViewModel", "Error al cargar reseñas (live)", throwable)
//                _uiState.update {
//                    it.copy(
//                        reviews = emptyList(),
//                        isLoading = false,
//                        errorMessage = throwable.message ?: "Error al cargar reseñas"
//                    )
//                }
//            }
//        }
//    }


        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = reviewRepository.getReviews()
            result.fold(
                onSuccess = { reviews ->
                    // Logs para depuración
                    if (reviews.isEmpty()) {
                        Log.w("HomeViewModel", "getReviews() returned an empty list")
                    } else {
                        reviews.forEach { r ->
                            Log.d(
                                "HomeViewModel",
                                "Review loaded: id=${r.id}, placeName=${r.placeName}, placeImage=${r.placeImage}, userId=${r.userId}"
                            )
                        }
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
                    // Loguea toda la excepción para ver la causa real
                    Log.e("HomeViewModel", "Error loading reviews", throwable)
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



    private fun loadGastrobares() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = gastroBarRepository.getGastroBares()
            result.fold(
                onSuccess = { gastrobares ->
                    _uiState.update {
                        it.copy(
                            gastrobares = gastrobares,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { throwable ->
                    _uiState.update {
                        it.copy(
                            gastrobares = emptyList(),
                            isLoading = false,
                            errorMessage = throwable.message ?: "Error al cargar gastrobares"
                        )
                    }
                }
            )
        }
    }

    val filteredReviews: List<ReviewInfo>
        get() = if (_uiState.value.searchQuery.isBlank()) {
            _uiState.value.reviews
        } else {
            _uiState.value.reviews.filter { review ->
                review.placeName.contains(_uiState.value.searchQuery, ignoreCase = true) ||
                        review.reviewText.contains(_uiState.value.searchQuery, ignoreCase = true)
            }
        }

    fun sendOrDeleteReviewLike(reviewId: String, userId: String) {
        viewModelScope.launch {
            val currentState = _uiState.value
            val review = currentState.reviews.find { it.id == reviewId }

            if (review == null) {
                // No se encontró la review
                return@launch
            }

            val result = reviewRepository.sendOrDeleteReviewLike(reviewId, userId)
            if (result.isSuccess) {
                _uiState.update { state ->
                    state.reviews.map { r ->
                        if (r.id == reviewId) {
                            if (r.liked) {
                                // Ya estaba like → quitar like
                                r.copy(likes = r.likes - 1, liked = false)
                            } else {
                                // No estaba like → poner like
                                r.copy(likes = r.likes + 1, liked = true)
                            }
                        } else r
                    }.let { updatedReviews ->
                        state.copy(reviews = updatedReviews)
                    }
                }
            }
        }
    }
}
