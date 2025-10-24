package com.example.myapplication.ui.mainuser

import com.example.myapplication.data.ReviewInfo
import com.example.myapplication.data.UserInfo

data class MainUserState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val user: UserInfo? = null,
    val reviews: List<ReviewInfo> = emptyList(),
    val deletingId: String? = null,        // id del review en proceso de eliminación
    val editing: EditingState? = null   // si no es null, estamos editando
)

data class EditingState(
    val reviewId: String,
    val originalText: String,
    val newText: String = originalText
)
