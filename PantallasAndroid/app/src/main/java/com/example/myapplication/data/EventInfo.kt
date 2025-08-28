package com.example.myapplication.data

import androidx.annotation.DrawableRes

data class EventInfo(
    val date: String,
    val time: String,
    val title: String,
    @DrawableRes val eventImage: Int
)
