//package com.example.myapplication.ui.mainuser
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.myapplication.data.ReviewInfo
//import com.example.myapplication.data.auth.CurrentUserProvider
//import com.example.myapplication.data.dtos.CreateReviewDto
//import com.example.myapplication.data.repository.ReviewRepository
//import com.example.myapplication.data.repository.UserRepository
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class MainUserViewModel @Inject constructor(
//    private val currentUserProvider: CurrentUserProvider,
//    private val userRepository: UserRepository,
//    private val reviewRepository: ReviewRepository
//) : ViewModel() {
//
//    private val _uiState = MutableStateFlow(MainUserState(isLoading = true))
//    val uiState = _uiState.asStateFlow()
//
//    init {
//        load()
//    }
//
//    fun load() {
//        viewModelScope.launch {
//            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
//            try {
//                val userId = currentUserProvider.currentUserId()
//                    ?: error("No hay usuario autenticado")
//
//                // Traer usuario (para cabecera) y sus reviews
//                val userResult = userRepository.getUserById(userId)
//                val reviewsResult = reviewRepository.getReviewsByUser(userId)
//
//                val user = userResult.getOrThrow()
//                val reviews = reviewsResult.getOrThrow()
//
//                _uiState.value = _uiState.value.copy(
//                    isLoading = false,
//                    user = user,
//                    reviews = reviews,
//                    errorMessage = null
//                )
//            } catch (e: Exception) {
//                _uiState.value = _uiState.value.copy(
//                    isLoading = false,
//                    errorMessage = e.message ?: "Error inesperado"
//                )
//            }
//        }
//    }
//
//    fun deleteReview(id: Int) {
//        viewModelScope.launch {
//            // Optimista: marcamos eliminando
//            _uiState.value = _uiState.value.copy(deletingId = id)
//            try {
//                reviewRepository.deleteReview(id).getOrThrow()
//                // Remove local
//                _uiState.value = _uiState.value.copy(
//                    deletingId = null,
//                    reviews = _uiState.value.reviews.filterNot { it.id == id }
//                )
//            } catch (e: Exception) {
//                _uiState.value = _uiState.value.copy(
//                    deletingId = null,
//                    errorMessage = e.message ?: "No se pudo borrar la reseña"
//                )
//            }
//        }
//    }
//
//    fun startEdit(review: ReviewInfo) {
//        _uiState.value = _uiState.value.copy(
//            editing = EditingState(reviewId = review.id, originalText = review.reviewText)
//        )
//    }
//
//    fun updateEditingText(newText: String) {
//        val editing = _uiState.value.editing ?: return
//        _uiState.value = _uiState.value.copy(editing = editing.copy(newText = newText))
//    }
//
//    fun confirmEdit(onSuccess: (() -> Unit)? = null) {
//        val editing = _uiState.value.editing ?: return
//        viewModelScope.launch {
//            try {
//                // 1) Buscar el review actual
//                val current = _uiState.value.reviews.firstOrNull { it.id == editing.reviewId }
//                    ?: throw IllegalStateException("No se encontró la reseña a editar")
//
//                // Si tu ReviewInfo NO tiene userId, usa el del usuario en estado:
//                // val userIdForDto = _uiState.value.user?.id ?: error("Falta userId para el DTO")
//                val userIdForDto = current.userId   // <- usa este si ReviewInfo sí lo trae
//
//                // 2) Construir el DTO SIN pasar parentReviewId (queda null por default)
//                val dto = CreateReviewDto(
//                    userId = userIdForDto,
//                    placeName = current.placeName,
//                    reviewText = editing.newText,
//                    parentReviewId = null
//                )
//
//                // 3) Llamar al repo con la firma correcta
//                reviewRepository.updateReview(
//                    id = editing.reviewId,
//                    review = dto
//                ).getOrThrow()
//
//                // 4) Actualizar la lista local
//                val updated = _uiState.value.reviews.map {
//                    if (it.id == editing.reviewId) it.copy(reviewText = editing.newText) else it
//                }
//                _uiState.value = _uiState.value.copy(reviews = updated, editing = null)
//                onSuccess?.invoke()
//
//            } catch (e: Exception) {
//                _uiState.value = _uiState.value.copy(
//                    errorMessage = e.message ?: "No se pudo editar la reseña"
//                )
//            }
//        }
//    }
//
//
//    fun cancelEdit() {
//        _uiState.value = _uiState.value.copy(editing = null)
//    }
//
//    fun dismissError() {
//        _uiState.value = _uiState.value.copy(errorMessage = null)
//    }
//}
