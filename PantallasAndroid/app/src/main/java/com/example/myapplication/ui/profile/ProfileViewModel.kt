package com.example.myapplication.ui.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.datasource.AuthRemoteDataSource
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
    private val storageRepository: StorageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ProfileState(
            userName = authRepository.currentUser?.displayName ?: "",
            userEmail = authRepository.currentUser?.email ?: "",
            profilePicUrl = authRepository.currentUser?.photoUrl?.toString() ?: ""
        )
    )
    val uiState: StateFlow<ProfileState> = _uiState

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

}

