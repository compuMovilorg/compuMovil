package com.example.myapplication.ui.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.auth.CurrentUserProvider
import com.example.myapplication.data.datasource.AuthRemoteDataSource
import com.example.myapplication.data.repository.ReviewRepository
import com.example.myapplication.data.repository.StorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRemoteDataSource,
    private val storageRepository: StorageRepository,
    private val currentUserProvider: CurrentUserProvider,
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ProfileState(
            userName = authRepository.currentUser?.displayName ?: "",
            userEmail = authRepository.currentUser?.email ?: "",
            profilePicUrl = authRepository.currentUser?.photoUrl?.toString() ?: ""
        )
    )
    val uiState: StateFlow<ProfileState> = _uiState

    init {
        loadUserReviews()
    }

    fun refreshProfile() {
        _uiState.update {
            it.copy(
                userName = authRepository.currentUser?.displayName ?: "",
                userEmail = authRepository.currentUser?.email ?: "",
                profilePicUrl = authRepository.currentUser?.photoUrl?.toString() ?: ""
            )
        }
    }
    fun uploadImageToFirebase(uri: Uri) {
        viewModelScope.launch {
            val result = storageRepository.uploadProfileImg(uri)
            if (result.isSuccess) {
                authRepository.updateProfileImage(result.getOrNull() ?: "")
                refreshProfile()
            }
        }
    }

    private fun loadUserReviews() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingReviews = true, reviewsError = null) }
            try {
                val userId = currentUserProvider.currentUserId()
                if (userId == null) {
                    _uiState.update {
                        it.copy(
                            isLoadingReviews = false,
                            reviewsError = "No se encontró el usuario actual"
                        )
                    }
                    return@launch
                }

                _uiState.update { it.copy(userId = userId) }

                val result = reviewRepository.getReviewsByUser(userId)
                result.fold(
                    onSuccess = { reviews ->
                        _uiState.update {
                            it.copy(
                                reviews = reviews,
                                isLoadingReviews = false,
                                reviewsError = null
                            )
                        }
                    },
                    onFailure = { throwable ->
                        _uiState.update {
                            it.copy(
                                reviews = emptyList(),
                                isLoadingReviews = false,
                                reviewsError = throwable.message
                                    ?: "Error al cargar reseñas"
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoadingReviews = false,
                        reviewsError = e.message ?: "Error al cargar reseñas"
                    )
                }
            }
        }
    }
}