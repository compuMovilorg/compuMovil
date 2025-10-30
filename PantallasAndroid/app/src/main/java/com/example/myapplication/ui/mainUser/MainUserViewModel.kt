package com.example.myapplication.ui.mainuser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.ReviewInfo
import com.example.myapplication.data.auth.CurrentUserProvider
import com.example.myapplication.data.dtos.CreateReviewDto
import com.example.myapplication.data.repository.ReviewRepository
import com.example.myapplication.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainUserViewModel @Inject constructor(
    private val currentUserProvider: CurrentUserProvider,
    private val userRepository: UserRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUserState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val userId = try {
                    currentUserProvider.currentUserId()
                        ?: throw IllegalStateException("No hay usuario autenticado")
                } catch (e: NotImplementedError) {
                    // manejo explícito para el provider sin implementar
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Funcionalidad de autenticación no implementada"
                    )
                    return@launch
                }

                // Traer usuario (para cabecera) y sus reviews
                val userResult = userRepository.getUserById(userId)
                val reviewsResult = reviewRepository.getReviewsByUser(userId)

                val user = userResult.getOrThrow()
                val reviews = reviewsResult.getOrThrow()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    user = user,
                    reviews = reviews,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Error inesperado"
                )
            }
        }
    }


    /**
     * El parámetro id ahora es String (como en Firebase).
     */
    fun deleteReview(id: String) {
        viewModelScope.launch {
            // Verificar que el review pertenece al usuario logueado
            val currentUserId = currentUserProvider.currentUserId()
            val review = _uiState.value.reviews.firstOrNull { it.id == id }
            if (review == null) {
                _uiState.value = _uiState.value.copy(errorMessage = "Reseña no encontrada")
                return@launch
            }
            if (currentUserId == null || review.userId != currentUserId) {
                _uiState.value = _uiState.value.copy(errorMessage = "No autorizado para borrar esta reseña")
                return@launch
            }

            // Optimista: marcamos eliminando
            _uiState.value = _uiState.value.copy(deletingId = id, errorMessage = null)
            try {
                // Asumo deleteReview acepta String id
                reviewRepository.deleteReview(id).getOrThrow()
                // Remove local
                _uiState.value = _uiState.value.copy(
                    deletingId = null,
                    reviews = _uiState.value.reviews.filterNot { it.id == id }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    deletingId = null,
                    errorMessage = e.message ?: "No se pudo borrar la reseña"
                )
            }
        }
    }

    /**
     * Inicia la edición — ahora review.id es String.
     * Se definen newText en el startEdit.
     */
    fun startEdit(review: ReviewInfo) {
        viewModelScope.launch {
            val currentUserId = currentUserProvider.currentUserId()
            if (currentUserId == null || review.userId != currentUserId) {
                _uiState.value = _uiState.value.copy(errorMessage = "No autorizado para editar esta reseña")
                return@launch
            }

            // Creamos el EditingState correctamente (no ReviewInfo)
            _uiState.value = _uiState.value.copy(
                editing = EditingState(
                    reviewId = review.id,
                    originalText = review.reviewText,
                    newText = review.reviewText
                ),
                errorMessage = null
            )
        }
    }

    fun updateEditingText(newText: String) {
        val editing = _uiState.value.editing ?: return
        _uiState.value = _uiState.value.copy(editing = editing.copy(newText = newText))
    }

    /**
     * confirmEdit usa reviewId: String y hace validaciones con userId String.
     */
    fun confirmEdit(onSuccess: (() -> Unit)? = null) {
        val editing = _uiState.value.editing ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val currentUserId = currentUserProvider.currentUserId()
                    ?: throw IllegalStateException("No hay usuario autenticado")

                // 1) Buscar el review actual
                val current = _uiState.value.reviews.firstOrNull { it.id == editing.reviewId }
                    ?: throw IllegalStateException("No se encontró la reseña a editar")

                // Verificar que el review realmente pertenezca al usuario logueado
                if (current.userId != currentUserId) {
                    throw IllegalStateException("No autorizado para editar esta reseña")
                }

                // 2) Construir el DTO (si tu repo pide otro DTO, cambia acá)
                val dto = CreateReviewDto(
                    userId = current.userId,
                    placeName = current.placeName,
                    reviewText = editing.newText,
                    placeImage = null,
                    parentReviewId = null
                )

                // 3) Llamar al repo con la firma correcta (asumo id: String)
                reviewRepository.updateReview(id = editing.reviewId, review = dto).getOrThrow()

                // 4) Actualizar la lista local
                val updated = _uiState.value.reviews.map {
                    if (it.id == editing.reviewId) it.copy(reviewText = editing.newText) else it
                }
                _uiState.value = _uiState.value.copy(reviews = updated, editing = null, isLoading = false)
                onSuccess?.invoke()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "No se pudo editar la reseña"
                )
            }
        }
    }

    fun cancelEdit() {
        _uiState.value = _uiState.value.copy(editing = null)
    }

    fun dismissError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
