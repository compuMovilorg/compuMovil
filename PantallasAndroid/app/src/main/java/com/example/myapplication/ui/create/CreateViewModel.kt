package com.example.myapplication.ui.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import com.example.myapplication.R
import com.example.myapplication.data.repository.ReviewRepository
import com.example.myapplication.data.repository.GastroBarRepository
import com.example.myapplication.data.repository.UserRepository
import com.example.myapplication.data.dtos.CreateReviewDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val gastroBarRepository: GastroBarRepository,
    private val userRepository: UserRepository
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

    /**
     * Crea una review:
     * - obtiene el userId desde UserRepository (userRepository.getCurrentUserId())
     * - si no hay userId, setea error en el estado (usuario no autenticado)
     * - llama a reviewRepository.createReview con userId (String)
     */
    fun createReview() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, error = null) }

            // 1) Obtener userId desde UserRepository
            val userId: String? = try {
                // ADAPTA según tu UserRepository; aquí asumimos que existe:
                userRepository.getCurrentUserId()
            } catch (e: Exception) {
                null
            }

            if (userId.isNullOrBlank()) {
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        error = "No se encontró usuario autenticado. Inicia sesión para poder crear una reseña."
                    )
                }
                return@launch
            }

            // 2) Validaciones mínimas
            val placeName = uiState.value.placeName.ifBlank { null }
            val reviewText = uiState.value.reviewText.ifBlank { null }

            if (placeName == null || reviewText == null) {
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        error = "Completa el nombre del lugar y el texto de la reseña antes de enviar."
                    )
                }
                return@launch
            }

            val createReviewDto = CreateReviewDto(
                userId = userId,
                placeName = placeName,
                reviewText = reviewText,
                placeImage = null,
                parentReviewId = uiState.value.parentReviewId
            )

            // 4) Llamada al repositorio
            val result = try {
                reviewRepository.createReview(
                    userId = createReviewDto.userId,
                    placeName = createReviewDto.placeName,
                    reviewText = createReviewDto.reviewText,
                    parentReviewId = createReviewDto.parentReviewId
                )
            } catch (e: Exception) {
                _uiState.update { it.copy(isSubmitting = false, error = e.message ?: "Error inesperado") }
                return@launch
            }

            // 5) Resultado
            if (result.isSuccess) {
                _uiState.update { it.copy(isSubmitting = false, navigateBack = true) }
            } else {
                _uiState.update {
                    it.copy(isSubmitting = false, error = result.exceptionOrNull()?.message ?: "Error al crear reseña")
                }
            }
        }
    }

    // ahora con String para el id del gastrobar
    fun onSelectGastroBar(id: String, name: String) {
        _uiState.update { it.copy(selectedGastroBarId = id, placeName = name) }
    }
}
