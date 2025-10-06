package com.example.myapplication.ui.detailBar

import com.example.myapplication.data.GastroBar
import com.example.myapplication.data.ReviewInfo

data class DetailGastroBarUiState(
    val gastroBar: GastroBar? = null,
    val reviews: List<ReviewInfo> = emptyList(),
    val reviewsReplies: List<Pair<Int, List<ReviewInfo>>> = emptyList(),
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
)
