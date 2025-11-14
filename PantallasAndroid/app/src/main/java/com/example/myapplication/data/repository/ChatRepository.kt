package com.example.myapplication.data.repository

import android.util.Log
import com.example.myapplication.data.ChatMessageInfo
import com.example.myapplication.data.datasource.AuthRemoteDataSource
import com.example.myapplication.data.datasource.impl.firestore.ChatFireStoreDataSourceImpl
import com.example.myapplication.data.dtos.CreateChatMessageDto
import com.example.myapplication.data.dtos.toChatMessageInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val chatRemoteDataSource: ChatFireStoreDataSourceImpl,
    private val authRemoteDataSource: AuthRemoteDataSource
) {

    private val TAG = "ChatRepository"

    /**
     * Obtiene la conversación entre el usuario autenticado y targetUserId
     */
    suspend fun getConversationWith(targetUserId: String): Result<List<ChatMessageInfo>> {
        val currentUserId = authRemoteDataSource.currentUser?.uid ?: ""

        if (currentUserId.isBlank()) {
            return Result.failure(IllegalStateException("Usuario no autenticado"))
        }

        return try {
            val dtos = chatRemoteDataSource.getConversation(currentUserId, targetUserId)
            val messagesInfo = dtos.map { it.toChatMessageInfo() } // <- igual que toReviewInfo()
            Log.d(TAG, "✅ Se obtuvieron ${messagesInfo.size} mensajes con $targetUserId")
            Result.success(messagesInfo)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error en getConversationWith: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * Envía un mensaje al usuario targetUserId desde el usuario autenticado
     */
    suspend fun sendMessageTo(targetUserId: String, text: String): Result<Unit> {
        val currentUserId = authRemoteDataSource.currentUser?.uid ?: ""

        if (currentUserId.isBlank()) {
            return Result.failure(IllegalStateException("Usuario no autenticado"))
        }

        if (text.isBlank()) {
            return Result.failure(IllegalArgumentException("Mensaje vacío"))
        }

        return try {
            val dto = CreateChatMessageDto(
                senderId = currentUserId,
                receiverId = targetUserId,
                text = text
            )

            chatRemoteDataSource.sendMessage(dto)
            Log.d(TAG, "✅ Mensaje enviado a $targetUserId")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error en sendMessageTo: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * Devuelve un Flow en tiempo real, igual que getReviewsLive() pero para una sola conversación
     */
    suspend fun listenConversationWith(targetUserId: String): Flow<List<ChatMessageInfo>> {
        val currentUserId = authRemoteDataSource.currentUser?.uid ?: ""

        if (currentUserId.isBlank()) {
            // Flow vacío si no hay usuario logueado
            return kotlinx.coroutines.flow.flow { emit(emptyList<ChatMessageInfo>()) }
        }

        return chatRemoteDataSource
            .listenConversation(currentUserId, targetUserId)
            .map { list -> list.map { it.toChatMessageInfo() } }
    }
}
