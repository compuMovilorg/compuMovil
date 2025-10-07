package com.example.myapplication.ui.create

import com.example.myapplication.data.GastroBar

data class CreateState(
    val placeName: String = "",
    val selectedGastroBarId: Int? = null,
    val gastrobares: List<GastroBar> = emptyList(),
    val isLoadingGastrobares: Boolean = false,
    val rating: Float = 0f,
    val reviewText: String = "",
    val selectedImages: List<Int> = emptyList(),
    val selectedTags: Set<String> = emptySet(),
    val navigateBack: Boolean = false,
    val error: String? = null
)

