package com.example.myapplication.ui.notification

data class NotificationState(
    val newFollowers: Boolean = false,
    val comments: Boolean = false,
    val likes: Boolean = false,
    val recommendations: Boolean = false
)
