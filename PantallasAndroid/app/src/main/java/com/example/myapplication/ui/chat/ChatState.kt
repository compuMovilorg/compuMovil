package com.example.myapplication.ui.chat

import com.example.myapplication.data.ChatMessageInfo
import com.example.myapplication.data.UserInfo

data class ChatState(
    val targetUser: UserInfo? = null,
    val messages: List<ChatMessageInfo> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val inputText: String = "",
    val targetUserId: String? = null,
    val currentUserId: String? = null
)
