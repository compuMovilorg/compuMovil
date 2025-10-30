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

@Composable
fun BarReviewsScreen(
    gastroBarId: String,
    gastroBarName: String? = null,
    onReviewClick: (String) -> Unit,
    onUserClick: (String) -> Unit
) {
    // Pasamos los parámetros al ViewModel usando hiltViewModel()
    val viewModel: BarReviewsViewModel = hiltViewModel()

    // Efecto para actualizar el estado si cambian los datos
    LaunchedEffect(gastroBarId, gastroBarName) {
        // Si el ViewModel aún no tiene ese gastroBar cargado, lo actualiza
        if (viewModel.uiState.value.gastroBarId != gastroBarId) {
            viewModel.reload()
        }
    }

    BarReviewsBody(
        viewModel = viewModel,
        onReviewClick = onReviewClick,
        onUserClick = onUserClick
    )
}

@Composable
fun BarReviewsBody(
    viewModel: BarReviewsViewModel,
    onReviewClick: (String) -> Unit,
    onUserClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
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
                            onReviewClick = { onReviewClick(it) },
                            onLikeClick = { reviewId, userId ->
                                viewModel.sendOrDeleteReviewLike(reviewId, userId)
                            },
                            onUserClick = { onUserClick(review.userId) }
                        )
                    }
                }
            }
        }
    }
}
