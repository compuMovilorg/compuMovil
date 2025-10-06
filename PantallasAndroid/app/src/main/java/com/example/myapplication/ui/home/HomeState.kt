package com.example.myapplication.ui.home

import android.os.Message
import com.example.myapplication.data.GastroBar
import com.example.myapplication.data.ReviewInfo

data class HomeState(
    val searchQuery: String = "",
    val gastrobares: List<GastroBar> = emptyList(),
    val reviews: List<ReviewInfo> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
