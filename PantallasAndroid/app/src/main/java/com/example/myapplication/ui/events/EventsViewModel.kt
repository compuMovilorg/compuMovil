package com.example.myapplication.ui.events

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.EventInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class EventViewModel @Inject constructor(
    initialEvents: List<EventInfo>
) : ViewModel() {

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
            return state.events.filter {
                state.searchQuery.isBlank() || it.title.contains(state.searchQuery, ignoreCase = true)
            }
        }
}
