// NotificationViewModel.kt
package com.example.myapplication.ui.notification

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class NotificationViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationState())
    val uiState: StateFlow<NotificationState> = _uiState

    val newFollowers: StateFlow<Boolean> get() = MutableStateFlow(_uiState.value.newFollowers)
    val comments: StateFlow<Boolean> get() = MutableStateFlow(_uiState.value.comments)
    val likes: StateFlow<Boolean> get() = MutableStateFlow(_uiState.value.likes)
    val recommendations: StateFlow<Boolean> get() = MutableStateFlow(_uiState.value.recommendations)

    fun setNewFollowers(value: Boolean) {
        _uiState.update { it.copy(newFollowers = value) }
    }

    fun setComments(value: Boolean) {
        _uiState.update { it.copy(comments = value) }
    }

    fun setLikes(value: Boolean) {
        _uiState.update { it.copy(likes = value) }
    }

    fun setRecommendations(value: Boolean) {
        _uiState.update { it.copy(recommendations = value) }
    }
}
