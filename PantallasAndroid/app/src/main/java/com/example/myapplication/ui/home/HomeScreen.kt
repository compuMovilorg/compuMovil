package com.example.myapplication.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.utils.ReviewCard

@Composable
fun HomeScreen(
    onReviewClick: (Int) -> Unit,
    onUserClick: (Int) -> Unit,
    modifier: Modifier,
    viewModel: HomeViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val reviews = remember(state.searchQuery, state.reviews) { viewModel.filteredReviews }

    Box(
        modifier = Modifier
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

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(reviews) { review ->
                        ReviewCard(
                            onReviewClick = { onReviewClick(review.id) },
                            review = review,
                            modifier = Modifier.fillMaxWidth(),
                            onUserClick = {onUserClick(review.userId)}
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewScreenPreview() {
    HomeScreen(
        onReviewClick = {},
        modifier = Modifier.fillMaxSize(),
        onUserClick = {}
    )
}
