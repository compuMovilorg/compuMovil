package com.example.myapplication.ui.notification

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NotificationViewModel : ViewModel() {

    private val _newFollowers = MutableStateFlow(true)
    val newFollowers: StateFlow<Boolean> = _newFollowers

    private val _comments = MutableStateFlow(false)
    val comments: StateFlow<Boolean> = _comments

    private val _likes = MutableStateFlow(true)
    val likes: StateFlow<Boolean> = _likes

    private val _recommendations = MutableStateFlow(false)
    val recommendations: StateFlow<Boolean> = _recommendations

    fun setNewFollowers(value: Boolean) {
        _newFollowers.value = value
    }

    fun setComments(value: Boolean) {
        _comments.value = value
    }

    fun setLikes(value: Boolean) {
        _likes.value = value
    }

    fun setRecommendations(value: Boolean) {
        _recommendations.value = value
    }
}
