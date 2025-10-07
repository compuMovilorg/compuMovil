package com.example.myapplication.ui.events

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.data.EventInfo
import com.example.myapplication.utils.SearchBarField
import com.example.myapplication.utils.TagChip

@Composable
fun BodyEventScreen(
    viewModel: EventViewModel,
    onEventClick: (EventInfo) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 110.dp)
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Eventos en vivo",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TagChip(
                text = "Hoy",
                isSelected = state.selectedTag == "Hoy",
                onClick = { viewModel.updateSelectedTag("Hoy") }
            )
            TagChip(
                text = "Próximo",
                isSelected = state.selectedTag == "Próximo",
                onClick = { viewModel.updateSelectedTag("Próximo") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        SearchBarField(
            modifier = Modifier.fillMaxWidth(),
            value = state.searchQuery,
            onValueChange = { viewModel.updateSearchQuery(it) },
            placeholder = "Buscar Evento..."
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(viewModel.filteredEvents) { event ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clickable { onEventClick(event) },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.LightGray)
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = event.eventImage),
                            contentDescription = "Flyer del evento",
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "${event.date} • ${event.time}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = event.title,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EventScreen(
    onEventClick: (EventInfo) -> Unit = {},
    viewModel: EventViewModel = hiltViewModel()
) {
    BodyEventScreen(
        viewModel = viewModel,
        onEventClick = onEventClick
    )
}
