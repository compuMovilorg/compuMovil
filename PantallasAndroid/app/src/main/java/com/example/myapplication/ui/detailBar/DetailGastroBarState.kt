package com.example.myapplication.ui.detailBar

import com.example.myapplication.data.GastroBar

data class DetailGastroBarUiState(
    val gastroBar: GastroBar = GastroBar(
        imagePlace = 0,
        id = 0,
        name = "",
        rating = 0f,
        reviewCount = 0,
        address = "",
        hours = "",
        cuisine = "",
        description = ""
    )
)