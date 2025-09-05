package com.example.myapplication.ui.create

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import com.example.myapplication.R
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

class CreateViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CreateState())
    val uiState: StateFlow<CreateState> = _uiState

    private val _navigateBack = MutableStateFlow(false)
    val navigateBack: StateFlow<Boolean> = _navigateBack

    fun onPlaceNameChanged(input: String) {
        _uiState.update { it.copy(placeName = input) }
    }

    fun onReviewTextChanged(input: String) {
        _uiState.update { it.copy(reviewText = input) }
    }

    fun onRatingChanged(input: Float) {
        _uiState.update { it.copy(rating = input) }
    }

    fun onAddImage(imageRes: Int = R.drawable.gastrobarimg1) {
        _uiState.update { it.copy(selectedImages = it.selectedImages + imageRes) }
    }

    fun onToggleTag(tag: String) {
        _uiState.update {
            if (tag in it.selectedTags) {
                it.copy(selectedTags = it.selectedTags - tag)
            } else {
                it.copy(selectedTags = it.selectedTags + tag)
            }
        }
    }

    fun submitReview() {
        val currentState = _uiState.value
        viewModelScope.launch {
            _navigateBack.value = true
        }
    }

    fun onNavigated() {
        _navigateBack.value = false
    }
}
