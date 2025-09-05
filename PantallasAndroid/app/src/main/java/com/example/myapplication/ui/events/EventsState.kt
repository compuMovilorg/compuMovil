package com.example.myapplication.ui.events

import com.example.myapplication.data.EventInfo

data class EventState(
    val searchQuery: String = "",
    val selectedTag: String = "Hoy",
    val events: List<EventInfo> = emptyList(),
    val filteredEvents: List<EventInfo> = emptyList()
)
