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
    gastroBarId: Int,
    gastroBarName: String? = null,
    onReviewClick: (Int) -> Unit,
    onUserClick: (Int) -> Unit
) {
    BarReviewsBody(
        gastroBarId = gastroBarId,
        gastroBarName = gastroBarName,
        onReviewClick = onReviewClick,
        onUserClick = onUserClick
    )
}

// -----------------------------------------------------------------------------
// BODY: maneja toda la lógica, estado, modifier y UI (similar al HomeScreen)
// -----------------------------------------------------------------------------
@Composable
fun BarReviewsBody(
    gastroBarId: Int,
    gastroBarName: String? = null,
    onReviewClick: (Int) -> Unit,
    onUserClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BarReviewsViewModel = hiltViewModel()
) {
    LaunchedEffect(gastroBarId) {
        viewModel.load(gastroBarId, gastroBarName)
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
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
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
                            onUserClick = { onUserClick(review.userId) }
                        )
                    }
                }
            }
        }
    }
}
