package com.example.myapplication.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.utils.ReviewCard
import com.example.myapplication.data.local.LocalReviewsProvider.Reviews


@Composable
fun HomeScreen(
    onReviewClick: (Int) -> Unit,
    modifier: Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredReviews = Reviews.filter { review ->
        review.placeName.contains(searchQuery, ignoreCase = true) ||
                review.reviewText.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top= 100.dp)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredReviews) { review ->
                ReviewCard(
                    onReviewClick = { onReviewClick(review.id) },
                    review = review,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewScreenPreview() {
    HomeScreen(
        onReviewClick = {},
        modifier = Modifier.fillMaxSize()
    )
}