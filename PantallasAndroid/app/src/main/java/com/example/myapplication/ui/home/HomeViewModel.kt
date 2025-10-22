package com.example.myapplication.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.ReviewInfo
import com.example.myapplication.data.repository.GastroBarRepository
import com.example.myapplication.data.repository.ReviewRepository
import com.example.myapplication.data.repository.StorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
                            Log.d("HomeViewModel", "Review loaded: id=${r.id}, placeName=${r.placeName}, placeImage=${r.placeImage}, userId=${r.userId}")
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
}
