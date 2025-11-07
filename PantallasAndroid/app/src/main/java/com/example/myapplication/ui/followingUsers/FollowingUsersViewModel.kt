package com.example.myapplication.ui.followingUsers

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "FollowingUsersVM"

@HiltViewModel
class FollowingUsersViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FollowingUsersState(isLoading = true))
    val uiState: StateFlow<FollowingUsersState> = _uiState

    init {
        reload()
    }

    fun reload() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                // 1) Traer IDs que sigue el usuario autenticado
                val idsResult = userRepository.getFollowingIds() // usa currentUser implícito
                if (idsResult.isFailure) {
                    val msg = idsResult.exceptionOrNull()?.message ?: "Error al cargar seguidos"
                    _uiState.update { it.copy(isLoading = false, errorMessage = msg, users = emptyList()) }
                    return@launch
                }
                val ids = idsResult.getOrNull().orEmpty()
                if (ids.isEmpty()) {
                    _uiState.update { it.copy(isLoading = false, users = emptyList(), errorMessage = null) }
                    return@launch
                }

                // 2) Traer perfiles en paralelo
                val users = ids.map { uid ->
                    async {
                        userRepository.getUserById(uid).getOrNull()
                    }
                }.awaitAll()
                    .filterNotNull()
                    .sortedBy { it.username.lowercase() }

                _uiState.update { it.copy(isLoading = false, users = users, errorMessage = null) }
            } catch (e: Exception) {
                Log.e(TAG, "reload error: ${e.message}", e)
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Error desconocido") }
            }
        }
    }
}
