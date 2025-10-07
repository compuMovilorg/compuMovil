package com.example.myapplication.ui.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import com.example.myapplication.R
import com.example.myapplication.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val gastroBarRepository: com.example.myapplication.data.repository.GastroBarRepository // ⬅️ agrega esto
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateState())
    val uiState: StateFlow<CreateState> = _uiState

    init {
        loadGastrobares()
    }

    private fun loadGastrobares() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingGastrobares = true, error = null) }
            val result = gastroBarRepository.getGastroBares()
            result.fold(
                onSuccess = { list ->
                    _uiState.update {
                        it.copy(
                            gastrobares = list,
                            isLoadingGastrobares = false
                        )
                    }
                },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(
                            isLoadingGastrobares = false,
                            error = e.message ?: "Error cargando gastrobares"
                        )
                    }
                }
            )
        }
    }

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

    fun createReview() {
        viewModelScope.launch {
            val result = reviewRepository.createReview(
                userId = 1,
                placeName = uiState.value.placeName,
                reviewText = uiState.value.reviewText,
                parentReviewId = null
            )

            if (result.isSuccess) {
                _uiState.update { it.copy(navigateBack = true) }
            } else {
                _uiState.update { it.copy(error = result.exceptionOrNull()?.message) }
            }
        }
    }

    fun onSelectGastroBar(id: Int, name: String) {
        _uiState.update { it.copy(selectedGastroBarId = id, placeName = name) }
    }
}



