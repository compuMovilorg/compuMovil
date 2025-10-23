package com.example.myapplication.ui.create

import android.net.Uri
import com.example.myapplication.data.GastroBar

data class CreateState(
    val placeName: String = "",
    val selectedGastroBarId: String? = null,
    val gastrobares: List<GastroBar> = emptyList(),
    val isLoadingGastrobares: Boolean = false,
    val rating: Float = 0f,
    val reviewText: String = "",
    val selectedImageUri: Uri? = null, // ✅ NUEVO
    val selectedTags: Set<String> = emptySet(),
    val navigateBack: Boolean = false,
    val isSubmitting: Boolean = false,
    val parentReviewId: String? = null,
    val error: String? = null
)
