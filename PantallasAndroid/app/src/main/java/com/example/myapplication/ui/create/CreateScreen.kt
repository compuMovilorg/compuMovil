package com.example.myapplication.ui.create

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.utils.AppButton
import com.example.myapplication.utils.StarRating

@Composable
fun CreateScreen(
    onSaveClick: (String, String, Float) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    CreateScreenBody(
        state = state,
        onPlaceNameChange = viewModel::onPlaceNameChanged,
        onReviewTextChange = viewModel::onReviewTextChanged,
        onRatingChange = viewModel::onRatingChanged,
        onAddImage = { viewModel.onAddImage() },
        onToggleTag = viewModel::onToggleTag,
        onSaveClick = onSaveClick,
        modifier = modifier
    )
}

@Composable
fun CreateScreenBody(
    state: CreateState,
    onPlaceNameChange: (String) -> Unit,
    onReviewTextChange: (String) -> Unit,
    onRatingChange: (Float) -> Unit,
    onAddImage: () -> Unit,
    onToggleTag: (String) -> Unit,
    onSaveClick: (String, String, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 110.dp)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Top
    ) {

        OutlinedTextField(
            value = state.placeName,
            onValueChange = onPlaceNameChange,
            label = { Text("Nombre del lugar") },
            placeholder = { Text("Buscar o seleccionar un gastrobar") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Calificar", style = MaterialTheme.typography.bodyMedium)
        StarRating(
            rating = state.rating,
            onRatingChanged = { onRatingChange(it.toFloat()) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Fotos
        Text(text = "Fotos")
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                AppButton(
                    texto = "+",
                    modifier = Modifier.size(64.dp),
                    onClick = onAddImage,
                    height = 64.dp,
                    fontSize = 24.sp
                )
            }
            items(state.selectedImages.size) { index ->
                Image(
                    painter = painterResource(id = state.selectedImages[index]),
                    contentDescription = "Imagen seleccionada",
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color.LightGray, RoundedCornerShape(8.dp))
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Reseña
        OutlinedTextField(
            value = state.reviewText,
            onValueChange = onReviewTextChange,
            label = { Text("Escribe tu reseña") },
            placeholder = { Text("Escribe sobre el lugar...") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Etiquetas
        Text(text = "Etiquetas")
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Cocteles", "Ambiente", "Servicio", "Precio").forEach { tag ->
                AssistChip(
                    onClick = { onToggleTag(tag) },
                    label = { Text(tag) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (tag in state.selectedTags) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        else Color.Transparent
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        AppButton(
            texto = "Publicar reseña",
            modifier = Modifier.fillMaxWidth(),
            onClick = { onSaveClick(state.placeName, state.reviewText, state.rating) },
            height = 60.dp,
            fontSize = 18.sp
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewCreateScreen() {
    CreateScreen(
        onSaveClick = { _, _, _ -> }
    )
}
