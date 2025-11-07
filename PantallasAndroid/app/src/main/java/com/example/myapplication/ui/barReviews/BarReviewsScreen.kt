package com.example.myapplication.ui.barReviews

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.myapplication.utils.ReviewCard

@Composable
fun BarReviewsScreen(
    viewModel: BarReviewsViewModel,
    gastroBarId: String,
    gastroBarName: String? = null,
    onReviewClick: (String) -> Unit,
    onUserClick: (String) -> Unit
) {
    // Cargar/re-cargar SOLO si cambia el id o el nombre
    LaunchedEffect(gastroBarId, gastroBarName) {
        if (viewModel.uiState.value.gastroBarId != gastroBarId) {
            viewModel.load(gastroBarId, gastroBarName)
        }
    }

    BarReviewsBody(
        viewModel = viewModel,
        gastroBarId = gastroBarId,
        onReviewClick = onReviewClick,
        onUserClick = onUserClick
    )
}

@Composable
fun BarReviewsBody(
    viewModel: BarReviewsViewModel,
    gastroBarId: String,
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
            .testTag("bar_reviews_screen_$gastroBarId")
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
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("bar_reviews_list")
                ) {
                    items(reviews, key = { it.id }) { review ->
                        // Contenedor genérico para contar/esperar ítems
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("review_item")
                        ) {
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
}
