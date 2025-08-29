package com.example.myapplication.ui.events

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.EventInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class EventViewModel(initialEvents: List<EventInfo>) : ViewModel() {

    private val _uiState = MutableStateFlow(EventState(events = initialEvents))
    val uiState: StateFlow<EventState> = _uiState

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun updateSelectedTag(tag: String) {
        _uiState.update { it.copy(selectedTag = tag) }
    }

    val filteredEvents: List<EventInfo>
        get() {
            val state = _uiState.value
            return if (state.searchQuery.isBlank()) {
                state.events
            } else {
                state.events.filter { it.title.contains(state.searchQuery, ignoreCase = true) }
            }
        }
    }

