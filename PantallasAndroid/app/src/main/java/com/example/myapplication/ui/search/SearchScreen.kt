package com.example.myapplication.ui.search

import GastroBarGrid
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.data.GastroBar
import com.example.myapplication.utils.SearchBarField

private const val TAG_SEARCH = "SearchScreen"

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onGastroBarClick: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(state.gastroBars.size) {
        Log.d(TAG_SEARCH, "GastroBares cargados=${state.gastroBars.size}")
    }

    BodySearchContent(
        state = state,
        onQueryChange = {
            Log.d(TAG_SEARCH, "query='$it'")
            viewModel.updateSearchQuery(it)
        },
        onRetry = {
            Log.d(TAG_SEARCH, "refresh()")
            viewModel.refresh()
        },
        onItemClick = { bar ->
            Log.d(TAG_SEARCH, "onItemClick id=${bar.id} name=${bar.name}")
            bar.id?.let {
                onGastroBarClick(it)
            } ?: run {
                Log.e(TAG_SEARCH, "No se puede navegar: id nulo")
            }
        }
    )
}

@Composable
private fun BodySearchContent(
    state: SearchState,
    onQueryChange: (String) -> Unit,
    onRetry: () -> Unit,
    onItemClick: (GastroBar) -> Unit
) {
    Column(Modifier.fillMaxSize().padding(8.dp)) {
        SearchBarField(
            modifier = Modifier.padding(top = 16.dp),
            value = state.searchQuery,
            onValueChange = onQueryChange,
            placeholder = "Buscar..."
        )

        Spacer(Modifier.height(8.dp))

        if (state.isLoading) {
            Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return
        }

        if (state.errorMessage != null) {
            Column(
                Modifier.fillMaxSize().padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(state.errorMessage, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
                Spacer(Modifier.height(12.dp))
                Button(onClick = onRetry) { Text("Reintentar") }
            }
            return
        }

        val items = if (state.searchQuery.isBlank()) state.gastroBars
        else state.gastroBars.filter { it.name.contains(state.searchQuery, ignoreCase = true) }

        LaunchedEffect(items.size, state.searchQuery) {
            Log.d(TAG_SEARCH, "items filtrados=${items.size} query='${state.searchQuery}'")
        }

        if (items.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                Text("No se encontraron resultados")
            }
        } else {
            GastroBarGrid(
                gastroBars = items,
                modifier = Modifier.fillMaxSize(),
                onGridItemClick = onItemClick
            )
        }
    }
}
