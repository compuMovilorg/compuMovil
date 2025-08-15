package com.example.myapplication.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.utils.ReviewCard
import com.example.myapplication.utils.SearchBarField
import com.example.myapplication.data.local.LocalReviewsProvider.Reviews

@Composable
fun ReviewScreen(
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
            .padding(16.dp)
    ) {
        // Barra de búsqueda con estado
        SearchBarField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = "Buscar lugares...",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Lista de reseñas filtradas
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredReviews) { review ->
                ReviewCard(
                    userImage = painterResource(id = review.userImage),
                    // Se pasa el valor del campo 'name' al parámetro 'userName' de la tarjeta,
                    // pero no se usa para filtrar.
                    userName = review.name,
                    placeName = review.placeName,
                    reviewText = review.reviewText,
                    likes = review.likes,
                    comments = review.comments
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewScreenPreview() {
    ReviewScreen(
        modifier = Modifier.fillMaxSize()
    )
}