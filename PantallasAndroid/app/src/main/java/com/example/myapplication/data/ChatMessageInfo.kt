package com.example.myapplication.data

data class ChatMessageInfo(
    val id: String,
    val senderId: String,
    val receiverId: String,
    val text: String,
    val senderName: String,
    val senderImage: String,
    val createdAt: String
)
