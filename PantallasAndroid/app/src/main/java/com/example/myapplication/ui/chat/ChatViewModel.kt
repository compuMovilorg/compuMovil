package com.example.myapplication.ui.chat

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.ChatRepository
import com.example.myapplication.data.repository.UserRepository
import com.example.myapplication.data.datasource.AuthRemoteDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
    private val authRemoteDataSource: AuthRemoteDataSource,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatState())
    val uiState: StateFlow<ChatState> = _uiState

    private val currentUserId: String? = authRemoteDataSource.currentUser?.uid
    private var targetUserId: String? = null

    init {
        targetUserId = savedStateHandle["userId"]
        Log.d("ChatVM", "Inicializando ChatViewModel con targetUserId=$targetUserId")

        when {
            currentUserId.isNullOrBlank() -> {
                Log.e("ChatVM", "Usuario no autenticado")
                _uiState.update {
                    it.copy(
                        errorMessage = "Debes iniciar sesión para chatear",
                        isLoading = false
                    )
                }
            }

            targetUserId.isNullOrBlank() -> {
                Log.e("ChatVM", "No se recibió userId para el chat")
                _uiState.update {
                    it.copy(
                        errorMessage = "Usuario de chat no encontrado",
                        isLoading = false
                    )
                }
            }

            else -> {
                // caso OK: hay usuario logueado y userId destino
                _uiState.update {
                    it.copy(
                        currentUserId = currentUserId,
                        targetUserId = targetUserId
                    )
                }

                loadTargetUser(targetUserId!!)
                loadMessages()
                // si quieres tiempo real luego:
                // startListeningConversation()
            }
        }
    }

    fun reload() {
        Log.d("ChatVM", "Recargando chat con targetUserId=$targetUserId")
        val tId = targetUserId
        if (!tId.isNullOrBlank()) {
            loadTargetUser(tId)
            loadMessages()
        }
    }

    private fun loadTargetUser(userId: String) {
        viewModelScope.launch {
            Log.d("ChatVM", "loadTargetUser(id=$userId)")
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val result = userRepository.getUserById(userId)
                result.fold(
                    onSuccess = { user ->
                        Log.d("ChatVM", "Usuario destino obtenido: ${user.username}")
                        _uiState.update { st ->
                            st.copy(targetUser = user)
                        }
                    },
                    onFailure = { e ->
                        Log.e("ChatVM", "Error al obtener usuario destino", e)
                        _uiState.update {
                            it.copy(errorMessage = "Error al obtener usuario de chat")
                        }
                    }
                )
            } catch (e: Exception) {
                Log.e("ChatVM", "Error inesperado en loadTargetUser", e)
                _uiState.update {
                    it.copy(errorMessage = e.message ?: "Error desconocido")
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun loadMessages() {
        val targetId = targetUserId ?: return

        viewModelScope.launch {
            Log.d("ChatVM", "loadMessages(targetId=$targetId)")
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = chatRepository.getConversationWith(targetId)
            result.fold(
                onSuccess = { messages ->
                    Log.d("ChatVM", "Mensajes obtenidos: ${messages.size}")
                    _uiState.update {
                        it.copy(
                            messages = messages,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { e ->
                    Log.e("ChatVM", "Error en getConversationWith", e)
                    _uiState.update {
                        it.copy(
                            messages = emptyList(),
                            isLoading = false,
                            errorMessage = e.message ?: "Error al cargar conversación"
                        )
                    }
                }
            )
        }
    }

    fun onMessageChange(newText: String) {
        _uiState.update { it.copy(inputText = newText) }
    }

    fun sendMessage() {
        val targetId = targetUserId ?: return
        val text = _uiState.value.inputText.trim()
        if (text.isBlank()) {
            Log.d("ChatVM", "Mensaje vacío, no se envía")
            return
        }

        viewModelScope.launch {
            Log.d("ChatVM", "Enviando mensaje a $targetId: $text")
            val result = chatRepository.sendMessageTo(targetId, text)
            result.onSuccess {
                _uiState.update { it.copy(inputText = "") }
                loadMessages()
            }.onFailure { e ->
                Log.e("ChatVM", "Error al enviar mensaje", e)
                _uiState.update {
                    it.copy(errorMessage = e.message ?: "No se pudo enviar el mensaje")
                }
            }
        }
    }
}


    // Opcional: tiempo real usando listenConversationWith
    /*
    fun startListeningConversation() {
        val targetId = targetUserId ?: return

        viewModelScope.launch {
            chatRepository.listenConversationWith(targetId).collect { messages ->
                _uiState.update { it.copy(messages = messages) }
            }
        }
    }
    */

