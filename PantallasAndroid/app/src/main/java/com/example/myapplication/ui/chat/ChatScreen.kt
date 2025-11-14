package com.example.myapplication.ui.chat

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.data.ChatMessageInfo
import com.example.myapplication.utils.ProfileAsyncImage

@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel()
) {
    Log.d("ChatScreen", "ChatScreen Composable inicializado")
    val state by viewModel.uiState.collectAsState()

    ChatScreenBody(
        state = state,
        onMessageChange = viewModel::onMessageChange,
        onSendClick = viewModel::sendMessage,
        onRetry = viewModel::reload,
        modifier = Modifier
    )
}

@Composable
fun ChatScreenBody(
    state: ChatState,
    onMessageChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    when {
        state.isLoading && state.messages.isEmpty() && state.targetUser == null -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .testTag("chat_screen_loading"),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        state.errorMessage != null && state.messages.isEmpty() -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .testTag("chat_screen_error"),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = state.errorMessage!!)
                    Spacer(Modifier.height(12.dp))
                    if (onRetry != null) {
                        Button(
                            onClick = onRetry,
                            modifier = Modifier.testTag("btn_chat_retry")
                        ) {
                            Text("Reintentar")
                        }
                    }
                }
            }
        }

        else -> {
            val user = state.targetUser
            val targetUsername = user?.username ?: "Chat"
            val displayName = user?.name ?: ""

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .testTag("chat_screen")
            ) {
                // 🔹 Header propio (sin TopAppBar)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 115.dp)
                        .testTag("chat_header"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar
                    ProfileAsyncImage(
                        profileImage = user?.profileImage ?: "",
                        size = 56,
                        modifier = Modifier.testTag("chat_header_avatar")
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = targetUsername,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.testTag("chat_header_username")
                        )
                        if (displayName.isNotBlank()) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = displayName,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.testTag("chat_header_name")
                            )
                        }
                    }
                }

                Divider()

                // Lista de mensajes
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    if (state.messages.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .testTag("chat_messages_empty"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No hay mensajes aún. ¡Envía el primero!")
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 8.dp)
                                .testTag("chat_messages_list"),
                            reverseLayout = true // últimos mensajes al final visualmente
                        ) {
                            items(
                                items = state.messages.sortedByDescending { it.createdAt },
                                key = { it.id }
                            ) { message ->
                                val isOwn = message.senderId == state.currentUserId
                                ChatMessageBubble(
                                    message = message,
                                    isOwn = isOwn
                                )
                            }
                        }
                    }

                    // Spinner pequeño cuando recarga con mensajes ya cargados
                    if (state.isLoading && state.messages.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(top = 8.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        }
                    }
                }

                // Input abajo
                Divider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .testTag("chat_input_row"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = state.inputText,
                        onValueChange = onMessageChange,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_input"),
                        placeholder = { Text("Escribe un mensaje...") },
                        maxLines = 4
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onSendClick,
                        modifier = Modifier
                            .height(56.dp)
                            .testTag("btn_chat_send")
                    ) {
                        Text("Enviar")
                    }
                }
            }
        }
    }
}

@Composable
fun ChatMessageBubble(
    message: ChatMessageInfo,
    isOwn: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isOwn) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 2.dp,
            modifier = Modifier
                .widthIn(max = 280.dp)
                .testTag("chat_message_${message.id}"),
            color = if (isOwn) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                if (!isOwn) {
                    Text(
                        text = message.senderName,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                }
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
