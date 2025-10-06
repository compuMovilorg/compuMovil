package com.example.myapplication.ui.create

data class CreateState(
    val placeName: String = "",
    val reviewText: String = "",
    val rating: Float = 0f,
    val selectedImages: List<Int> = emptyList(),
    val selectedTags: List<String> = emptyList(),
    val navigateBack: Boolean = false,
    val error: String? = null
    )
