package com.example.myapplication.ui.home

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.ReviewInfo
import com.example.myapplication.data.local.LocalReviewsProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState

    init {
        _uiState.update { it.copy(reviews = LocalReviewsProvider.Reviews) }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
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

    fun getReviewById(id: Int): ReviewInfo? {
        return uiState.value.reviews.find { it.id == id }
    }
}