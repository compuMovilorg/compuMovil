package com.example.myapplication.ui.home

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.ReviewInfo
import com.example.myapplication.data.local.LocalReviewsProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val localReviewsProvider: LocalReviewsProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState

    private val reviews = localReviewsProvider.reviews

    init {
        _uiState.update { it.copy(reviews = reviews) }
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
