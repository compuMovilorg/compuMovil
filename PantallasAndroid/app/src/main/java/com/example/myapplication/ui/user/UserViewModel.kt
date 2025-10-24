package com.example.myapplication.ui.user

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.datasource.AuthRemoteDataSource
import com.example.myapplication.data.repository.ReviewRepository
import com.example.myapplication.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
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
    private val authRemoteDataSource: AuthRemoteDataSource,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserState())
    val uiState: StateFlow<UserState> = _uiState

    private val authenticatedUserId = FirebaseAuth.getInstance().currentUser?.uid
    private var viewedUserId: String? = null

    init {
        viewedUserId = savedStateHandle["userId"]
        Log.d("UserVM", "Inicializando UserViewModel con userId recibido = $viewedUserId")

        if (!viewedUserId.isNullOrBlank()) {
            Log.d("UserVM", "Cargando usuario con ID: $viewedUserId")
            loadUser(viewedUserId!!)
            loadReviews(viewedUserId!!)
        } else {
            Log.e("UserVM", "No se recibió un userId en los argumentos")
            _uiState.update { it.copy(errorMessage = "Usuario no encontrado") }
        }
    }

    fun reload() {
        Log.d("UserVM", "Recargando datos del usuario $viewedUserId")
        viewedUserId?.let {
            loadUser(it)
            loadReviews(it)
        }
    }

    fun loadUser(userId: String) {
        viewModelScope.launch {
            Log.d("UserVM", "Llamando loadUser con id=$userId")
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val userResult = userRepository.getUserById(userId, authenticatedUserId)
                userResult.fold(
                    onSuccess = { user ->
                        Log.d("UserVM", "Usuario obtenido correctamente: ${user.username}")
                        _uiState.update { it.copy(user = user) }
                    },
                    onFailure = { e ->
                        Log.e("UserVM", "Error al obtener usuario", e)
                        _uiState.update { it.copy(errorMessage = "Error al obtener usuario") }
                    }
                )
            } catch (e: Exception) {
                Log.e("UserVM", "Error inesperado", e)
                _uiState.update { it.copy(errorMessage = e.message ?: "Error desconocido") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun loadReviews(userId: String) {
        viewModelScope.launch {
            Log.d("UserVM", "Llamando loadReviews con id=$userId")
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val result = reviewRepository.getReviewsByUser(userId)
                result.fold(
                    onSuccess = { reviews ->
                        Log.d("UserViewModel", "Reseñas obtenidas: ${reviews.size}")
                        _uiState.update {
                            it.copy(
                                reviews = reviews,
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                    },
                    onFailure = { throwable ->
                        Log.e("UserViewModel", "Error en getReviewsByUser", throwable)
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
                Log.e("UserViewModel", "Excepción inesperada", e)
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun followOrUnfollowUser(targetUserId: String) {
        Log.d("UserVM", "Entrando a followOrUnfollowUser con targetUserId=$targetUserId")

        val currentUserId = authRemoteDataSource.currentUser?.uid
        if (currentUserId.isNullOrEmpty()) {
            Log.e("UserVM", "Usuario no autenticado")
            return
        }
        if (currentUserId == targetUserId) {
            Log.d("UserVM", "El usuario no puede seguirse a sí mismo")
            return
        }

        Log.d("UserVM", "Intentando seguir/desseguir userId=$targetUserId desde currentUserId=$currentUserId")

        viewModelScope.launch {
            try {
                val result = userRepository.followOrUnfollowUser(currentUserId, targetUserId)
                Log.d("UserVM", "Resultado del repo follow/unfollow: $result")

                if (result.isSuccess) {
                    _uiState.value = _uiState.value.copy(
                        user = _uiState.value.user?.copy(
                            followed = !(_uiState.value.user?.followed ?: false)
                        )
                    )
                    Log.d("UserVM", "UIState actualizado correctamente")
                } else {
                    Log.e("UserVM", "Error en follow/unfollow: ${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                Log.e("UserVM", "Excepción al intentar follow/unfollow", e)
            }
        }
    }
}
