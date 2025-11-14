package com.example.myapplication.data.datasource.impl.firestore

import android.util.Log
import com.example.myapplication.data.datasource.ChatRemoteDataSource
import com.example.myapplication.data.dtos.ChatMessageDto
import com.example.myapplication.data.dtos.CreateChatMessageDto
import com.example.myapplication.data.dtos.UserProfileDto
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatFireStoreDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
) : ChatRemoteDataSource {

    private val collectionName = "chats"
    private val TAG = "ChatFireStoreDS"

    private fun conversationIdFor(userId1: String, userId2: String): String {
        return if (userId1 < userId2) {
            "${userId1}_${userId2}"
        } else {
            "${userId2}_${userId1}"
        }
    }

    // Helper igual al de reviews
    private fun Any?.asStringOrNull(): String? {
        return when (this) {
            is String -> this
            is Number -> this.toString()
            else -> this?.toString()
        }
    }

    private fun mapDocToChatMessageDto(docId: String, data: Map<String, Any?>): ChatMessageDto {
        val senderId = data["senderId"].asStringOrNull() ?: ""
        val receiverId = data["receiverId"].asStringOrNull() ?: ""
        val text = data["text"].asStringOrNull() ?: ""

        val createdAtTimestamp = data["createdAt"] as? Timestamp
        val createdAtStr = createdAtTimestamp?.toDate()?.time?.toString() ?: ""

        // Si luego quieres guardar info del sender anidada tipo "sender": { ... }
        val senderMap = data["sender"] as? Map<*, *>
        val senderProfile = if (senderMap != null) {
            UserProfileDto(
                id = senderMap["id"].asStringOrNull() ?: "",
                username = senderMap["username"].asStringOrNull() ?: "",
                name = senderMap["name"].asStringOrNull() ?: "",
                profileImage = senderMap["profileImage"].asStringOrNull()
            )
        } else {
            UserProfileDto()
        }

        return ChatMessageDto(
            id = docId,
            senderId = senderId,
            receiverId = receiverId,
            text = text,
            createdAt = createdAtStr,
            sender = senderProfile
        )
    }

    override suspend fun getConversation(
        currentUserId: String,
        targetUserId: String
    ): List<ChatMessageDto> {
        val conversationId = conversationIdFor(currentUserId, targetUserId)
        Log.d(TAG, "getConversation -> conversationId=$conversationId")

        val snapshot = db.collection(collectionName)
            .document(conversationId)
            .collection("messages")
            .orderBy("createdAt")
            .get()
            .await()

        Log.d(TAG, "getConversation -> snapshot size=${snapshot.size()}")

        val list = mutableListOf<ChatMessageDto>()
        for (doc in snapshot.documents) {
            try {
                val data = doc.data ?: emptyMap<String, Any?>()
                @Suppress("UNCHECKED_CAST")
                val dto = mapDocToChatMessageDto(doc.id, data as Map<String, Any?>)
                list.add(dto)
            } catch (e: Exception) {
                Log.e(TAG, "Error parsing chat message doc ${doc.id}", e)
            }
        }

        Log.d(TAG, "getConversation -> total mensajes parseados=${list.size}")
        return list
    }

    override suspend fun sendMessage(message: CreateChatMessageDto) {
        val conversationId = conversationIdFor(message.senderId, message.receiverId)
        Log.d(TAG, "sendMessage -> conversationId=$conversationId")

        val messagesRef = db.collection(collectionName)
            .document(conversationId)
            .collection("messages")

        val data = hashMapOf(
            "senderId" to message.senderId,
            "receiverId" to message.receiverId,
            "text" to message.text,
            "createdAt" to FieldValue.serverTimestamp()
            // "sender" a futuro si quieres meter UserProfileDto
        )

        messagesRef.add(data).await()
    }

    override suspend fun listenConversation(
        currentUserId: String,
        targetUserId: String
    ): Flow<List<ChatMessageDto>> = callbackFlow {
        val conversationId = conversationIdFor(currentUserId, targetUserId)
        Log.d(TAG, "listenConversation -> conversationId=$conversationId")

        val listenerRegistration = db.collection(collectionName)
            .document(conversationId)
            .collection("messages")
            .orderBy("createdAt")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Error al escuchar conversación", error)
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot == null) {
                    Log.w(TAG, "Snapshot nulo en listenConversation()")
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                try {
                    val messages = snapshot.documents.mapNotNull { doc ->
                        try {
                            val data = doc.data ?: emptyMap<String, Any?>()
                            @Suppress("UNCHECKED_CAST")
                            mapDocToChatMessageDto(doc.id, data as Map<String, Any?>)
                        } catch (e: Exception) {
                            Log.e(TAG, "Error parseando doc ${doc.id} en listenConversation", e)
                            null
                        }
                    }
                    trySend(messages).isSuccess
                } catch (e: Exception) {
                    Log.e(TAG, "Error procesando snapshot de conversación", e)
                    close(e)
                }
            }

        awaitClose {
            listenerRegistration.remove()
        }
    }
}
