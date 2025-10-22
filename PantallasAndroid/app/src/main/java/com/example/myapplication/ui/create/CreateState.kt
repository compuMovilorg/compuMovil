package com.example.myapplication.ui.create

import com.example.myapplication.data.GastroBar

data class CreateState(
    val placeName: String = "",
    val selectedGastroBarId: String? = null,      // <-- String ahora
    val gastrobares: List<GastroBar> = emptyList(),
    val isLoadingGastrobares: Boolean = false,
    val rating: Float = 0f,
    val reviewText: String = "",
    val selectedImages: List<Int> = emptyList(),
    val selectedTags: Set<String> = emptySet(),
    val navigateBack: Boolean = false,
    val isSubmitting: Boolean = false,
    val parentReviewId: String? = null,           // <-- String? para respuestas
    val error: String? = null
)
