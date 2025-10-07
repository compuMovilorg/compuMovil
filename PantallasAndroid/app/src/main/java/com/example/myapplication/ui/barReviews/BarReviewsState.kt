package com.example.myapplication.ui.barReviews

import com.example.myapplication.data.ReviewInfo

data class BarReviewsState(
    val gastroBarId: Int? = null,
    val gastroBarName: String? = null,

    val searchQuery: String = "",
    val reviews: List<ReviewInfo> = emptyList(),

    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
