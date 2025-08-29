package com.example.myapplication.ui.home

import com.example.myapplication.data.ReviewInfo

data class HomeState(
    val searchQuery: String = "",
    val reviews: List<ReviewInfo> = emptyList()
)
