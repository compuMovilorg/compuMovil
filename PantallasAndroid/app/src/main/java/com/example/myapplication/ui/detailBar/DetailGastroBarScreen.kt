package com.example.myapplication.ui.detailBar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.myapplication.data.GastroBar
import com.example.myapplication.utils.AppButton
import com.example.myapplication.utils.StarRating

// -----------------------------------------------------------------------------
// PANTALLA DELGADA: solo delega al Body
// -----------------------------------------------------------------------------
@Composable
fun DetailGastroBarScreen(
    gastroBarId: Int,
    viewModel: DetailGastroBarViewModel = hiltViewModel(),
    onViewReviewsClick: (Int, String?) -> Unit // 👈 nuevo callback
) {
    DetailGastroBarBody(
        gastroBarId = gastroBarId.toString(),
        viewModel = viewModel,
        onViewReviewsClick = onViewReviewsClick
    )
}

@Composable
fun DetailGastroBarBody(
    gastroBarId: String,
    modifier: Modifier = Modifier,
    viewModel: DetailGastroBarViewModel,
    onViewReviewsClick: (Int, String?) -> Unit // 👈 pasa al body
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(gastroBarId) {
        viewModel.buscarGastro(gastroBarId.toInt())
    }

    state.gastroBar?.let { gastroBar ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            HeaderImage(imageUrl = gastroBar.imagePlace)
            PlaceInfoSection(gastroBar = gastroBar)

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppButton(
                    texto = "Calificar y Escribir reseña",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { /* TODO */ }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = {
                        // 👇 Navega a BarReviews con id y nombre
                        onViewReviewsClick(gastroBar.id.toInt(), gastroBar.name)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Ver reseñas")
                }
            }
        }
    } ?: run {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("GastroBar no encontrado")
        }
    }
}

// -----------------------------------------------------------------------------
// COMPONENTES DE UI REUTILIZABLES (sin cambios de lógica)
// -----------------------------------------------------------------------------
@Composable
fun HeaderImage(imageUrl: String?) {
    AsyncImage(
        model = imageUrl ?: "",
        contentDescription = "Gastrobar Image",
        contentScale = androidx.compose.ui.layout.ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    )
}

@Composable
fun PlaceInfoSection(gastroBar: GastroBar) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = gastroBar.name,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            fontSize = 24.sp
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            StarRating(rating = gastroBar.rating)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${gastroBar.rating} (${gastroBar.reviewCount} reseñas)",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        InfoRow(Icons.Default.LocationOn, gastroBar.address)
        Spacer(modifier = Modifier.height(8.dp))
        InfoRow(Icons.Default.Schedule, gastroBar.hours)
        Spacer(modifier = Modifier.height(8.dp))
        InfoRow(Icons.Default.Lightbulb, gastroBar.cuisine)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = gastroBar.description,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun InfoRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}
