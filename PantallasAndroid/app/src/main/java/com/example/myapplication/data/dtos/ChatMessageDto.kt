package com.example.myapplication.data.dtos

import com.example.myapplication.data.ChatMessageInfo

data class ChatMessageDto(
    val id: String,
    val senderId: String,
    val receiverId: String,
    val text: String,
    val createdAt: String,                  // igual que ReviewDto (String)
    val sender: UserProfileDto = UserProfileDto()  // info básica del remitente
) {
    constructor() : this(
        id = "",
        senderId = "",
        receiverId = "",
        text = "",
        createdAt = "",
        sender = UserProfileDto()
    )
}

// DTO para crear mensaje
data class CreateChatMessageDto(
    val senderId: String,
    val receiverId: String,
    val text: String
) {
    constructor() : this(senderId = "", receiverId = "", text = "")
}

fun ChatMessageDto.toChatMessageInfo(): ChatMessageInfo {
    return ChatMessageInfo(
        id = id,
        senderId = senderId,
        receiverId = receiverId,
        text = text,
        senderName = sender.name,
        senderImage = sender.profileImage ?: "",
        createdAt = createdAt
    )
}
