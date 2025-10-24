// ui/barReviews/BarReviewsScreen.kt
package com.example.myapplication.ui.barReviews

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.data.ReviewInfo
import com.example.myapplication.utils.ReviewCard

// -----------------------------------------------------------------------------
// PANTALLA DELGADA: solo delega al Body
// -----------------------------------------------------------------------------
@Composable
fun BarReviewsScreen(
    state: BarReviewsState,
    reviews: List<ReviewInfo>,
    onReviewClick: (String) -> Unit,          // ← String
    onUserClick: (String) -> Unit,            // ← String
    modifier: Modifier = Modifier
) {
    BarReviewsBody(
        state = state,
        reviews = reviews,
        onReviewClick = onReviewClick,
        onUserClick = onUserClick,
        modifier = modifier
    )
}

@Composable
fun BarReviewsBody(
    state: BarReviewsState,
    reviews: List<ReviewInfo>,
    onReviewClick: (String) -> Unit,          // ← String
    onUserClick: (String) -> Unit,            // ← String
    modifier: Modifier = Modifier,
) {


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
                            onUserClick = { onUserClick(review.userId) }
                        )
                    }
                }
            }
        }
    }
}


// -----------------------------------------------------------------------------
// PREVIEW
// -----------------------------------------------------------------------------
@Preview
@Composable
fun PreviewBarReviewsScreen() {
    val sampleReview = ReviewInfo(
        userImage = null,
        placeImage = null,
        userId = "user-1",
        id = "review-1",
        name = "Carlos",
        placeName = "Santa Juana Gastrobar",
        reviewText = "Excelente ambiente y cocteles deliciosos.",
        likes = 12,
        comments = 3
    )

    val sampleState = BarReviewsState(
        gastroBarId = "1",
        gastroBarName = "Santa Juana Gastrobar",
        reviews = listOf(sampleReview)
    )

    BarReviewsScreen(
        state = sampleState,
        reviews = sampleState.reviews,
        onReviewClick = {},
        onUserClick = {}
    )
}
