package com.example.myapplication.ui.user

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.ReviewInfo
import com.example.myapplication.data.repository.ReviewRepository
import com.example.myapplication.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserState())
    val uiState: StateFlow<UserState> = _uiState

    // Guarda el último userId para poder reintentar
    private var currentUserId: Int? = null

    init {
        val userId: Int? = savedStateHandle["userId"]
        currentUserId = userId
        if (userId != null) {
            android.util.Log.d("UserVM", "Cargando usuario con ID: $userId")
            loadUser(userId)
            loadReviews(userId)
        } else {
            android.util.Log.e("UserVM", "No se recibió un userId en los argumentos")
            _uiState.update { it.copy(errorMessage = "Usuario no encontrado") }
        }
    }

    fun reload() {
        currentUserId?.let {
            loadUser(it)
            loadReviews(it)
        }
    }

    fun loadUser(userId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val userResult = userRepository.getUserById(userId.toString())
                userResult.fold(
                    onSuccess = { user ->
                        _uiState.update { it.copy(user = user) }
                    },
                    onFailure = { e ->
                        android.util.Log.e("UserVM", "Error al obtener usuario", e)
                        _uiState.update { it.copy(errorMessage = "Error al obtener usuario") }
                    }
                )
            } catch (e: Exception) {
                android.util.Log.e("UserVM", "Error inesperado", e)
                _uiState.update { it.copy(errorMessage = e.message ?: "Error desconocido") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun loadReviews(userId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val result = reviewRepository.getReviewsByUser(userId)
                result.fold(
                    onSuccess = { reviews ->
                        android.util.Log.d("UserViewModel", "Reseñas obtenidas: ${reviews.size}")
                        _uiState.update {
                            it.copy(
                                reviews = reviews,
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                    },
                    onFailure = { throwable ->
                        android.util.Log.e("UserViewModel", "Error en getReviewsByUser", throwable)
                        _uiState.update {
                            it.copy(
                                reviews = emptyList(),
                                isLoading = false,
                                errorMessage = throwable.message
                                    ?: "Error al cargar reseñas del usuario"
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                android.util.Log.e("UserViewModel", "Excepción inesperada", e)
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    val userReviews: List<com.example.myapplication.data.ReviewInfo>
        get() = _uiState.value.reviews
}