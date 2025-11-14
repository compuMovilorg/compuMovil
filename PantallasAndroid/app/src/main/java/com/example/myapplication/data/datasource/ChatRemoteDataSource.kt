package com.example.myapplication.data.datasource

import com.example.myapplication.data.dtos.ChatMessageDto
import com.example.myapplication.data.dtos.CreateChatMessageDto
import kotlinx.coroutines.flow.Flow

interface ChatRemoteDataSource {

    // Obtener toda la conversación entre dos usuarios
    suspend fun getConversation(
        currentUserId: String,
        targetUserId: String
    ): List<ChatMessageDto>

    // Enviar un mensaje nuevo
    suspend fun sendMessage(
        message: CreateChatMessageDto
    )

    // Escuchar la conversación en tiempo real (estilo listenAllReviews)
    suspend fun listenConversation(
        currentUserId: String,
        targetUserId: String
    ): Flow<List<ChatMessageDto>>
}
