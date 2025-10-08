// ui/barReviews/BarReviewsScreen.kt
package com.example.myapplication.ui.barReviews

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.utils.ReviewCard

// -----------------------------------------------------------------------------
// PANTALLA DELGADA: solo delega al Body
// -----------------------------------------------------------------------------
@Composable
fun BarReviewsScreen(
    gastroBarId: String,                      // ← String
    gastroBarName: String? = null,
    onReviewClick: (String) -> Unit,          // ← String
    onUserClick: (String) -> Unit             // ← String
) {
    BarReviewsBody(
        gastroBarId = gastroBarId,
        gastroBarName = gastroBarName,
        onReviewClick = onReviewClick,
        onUserClick = onUserClick
    )
}

@Composable
fun BarReviewsBody(
    gastroBarId: String,                      // ← String
    gastroBarName: String? = null,
    onReviewClick: (String) -> Unit,          // ← String
    onUserClick: (String) -> Unit,            // ← String
    modifier: Modifier = Modifier,
    viewModel: BarReviewsViewModel = hiltViewModel()
) {
    // Si tu VM aún recibe Int, convierte aquí:
    LaunchedEffect(gastroBarId, gastroBarName) {
        val idInt = gastroBarId.toIntOrNull() ?: 0
        viewModel.load(idInt, gastroBarName)
    }

    val state by viewModel.uiState.collectAsState()
    val reviews = remember(state.searchQuery, state.reviews) { viewModel.filteredReviews }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 100.dp)
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
            state.errorMessage != null -> {
                Text(
                    text = state.errorMessage ?: "Error desconocido",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            state.reviews.isEmpty() -> {
                Text(
                    text = "Aún no hay reseñas para este lugar",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(reviews, key = { it.id }) { review ->
                        ReviewCard(
                            review = review,
                            modifier = Modifier.fillMaxWidth(),
                            onReviewClick = { onReviewClick(review.id) },
                            onUserClick = {
                                // Navega solo si tenemos authorId
                                review.userId?.let { onUserClick(it) }
                            }
                        )
                    }
                }
            }
        }
    }
}

