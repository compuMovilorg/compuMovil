package com.example.myapplication.ui.create

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.dtos.CreateReviewDto
import com.example.myapplication.data.repository.GastroBarRepository
import com.example.myapplication.data.repository.ReviewRepository
import com.example.myapplication.data.repository.StorageRepository
import com.example.myapplication.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val gastroBarRepository: GastroBarRepository,
    private val userRepository: UserRepository,
    private val storageRepository: StorageRepository
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

    fun onSelectImage(uri: Uri) {
        _uiState.update { it.copy(selectedImageUri = uri) }
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

    fun onSelectGastroBar(id: String, name: String) {
        _uiState.update { it.copy(selectedGastroBarId = id, placeName = name) }
    }

    /**
     * Crea una reseña:
     * - obtiene el userId desde UserRepository
     * - sube la imagen a Firebase Storage si existe
     * - llama a ReviewRepository para guardar todo en Firestore
     */
    fun createReview() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, error = null) }

            val userId = try {
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


            var imageUrl: String? = null
            uiState.value.selectedImageUri?.let { uri ->
                val uploadResult = storageRepository.uploadImage(uri)
                uploadResult.fold(
                    onSuccess = { url ->
                        imageUrl = url
                        Log.d("CreateVM", "✅ Imagen subida correctamente: $url")
                    },
                    onFailure = { e ->
                        Log.e("CreateVM", "❌ Error subiendo imagen: ${e.message}")
                    }
                )
            }

            // 🔹 Crear DTO con todos los datos
            val createReviewDto = CreateReviewDto(
                userId = userId,
                placeName = placeName,
                reviewText = reviewText,
                placeImage = imageUrl,
                parentReviewId = uiState.value.parentReviewId,
                gastroBarId = uiState.value.selectedGastroBarId
            )

            // 🔹 Enviar DTO completo al repositorio
            val result = try {
                reviewRepository.createReview(createReviewDto)
            } catch (e: Exception) {
                _uiState.update { it.copy(isSubmitting = false, error = e.message ?: "Error inesperado") }
                return@launch
            }

            if (result.isSuccess) {
                _uiState.update { it.copy(isSubmitting = false, navigateBack = true) }
            } else {
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        error = result.exceptionOrNull()?.message ?: "Error al crear reseña"
                    )
                }
            }
        }
    }
}
