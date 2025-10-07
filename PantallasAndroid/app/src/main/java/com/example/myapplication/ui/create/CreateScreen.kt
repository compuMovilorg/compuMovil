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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.utils.AppButton
import com.example.myapplication.utils.StarRating
import com.example.myapplication.ui.components.GastroBarPicker
import com.example.myapplication.data.GastroBar

@Composable
fun CreateScreen(
    modifier: Modifier = Modifier,
    viewModel: CreateViewModel = viewModel(),
    onSaveClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.navigateBack) {
        if (state.navigateBack) {
            onSaveClick()
        }
    }

    CreateScreenBody(
        state = state,
        onPlaceNameChange = viewModel::onPlaceNameChanged,
        // ⬇️ ahora el callback acepta (id, name) y casa con tu VM
        onSelectGastroBar = viewModel::onSelectGastroBar,
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
    onSelectGastroBar: (Int, String) -> Unit,
    onReviewTextChange: (String) -> Unit,
    onRatingChange: (Float) -> Unit,
    onAddImage: () -> Unit,
    onToggleTag: (String) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 110.dp)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Top
    ) {

        // ─────────────────────────────────────────────────────────────────────
        // Nombre del lugar -> AUTOCOMPLETE con Picker
        // ─────────────────────────────────────────────────────────────────────
        GastroBarPicker(
            items = state.gastrobares,                  // List<GastroBar> o tu tipo equivalente
            value = state.placeName,
            onValueChange = onPlaceNameChange,
            onSelect = { selected ->
                // ✅ Llamamos al VM con (id, name). El VM setea también placeName.
                onSelectGastroBar(selected.id, selected.name)
            },
            isLoading = state.isLoadingGastrobares,
            label = "Nombre del lugar",
            enabled = true,
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

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                        .clickable { onAddImage() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("+")
                }
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

        Spacer(modifier = Modifier.height(20.dp))

        // Reseña
        OutlinedTextField(
            value = state.reviewText,
            onValueChange = onReviewTextChange,
            label = { Text("Escribe tu reseña") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Etiquetas
        Text(text = "Etiquetas")
        Spacer(modifier = Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Cocteles", "Ambiente", "Servicio", "Precio").forEach { tag ->
                AssistChip(
                    onClick = { onToggleTag(tag) },
                    label = { Text(tag) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (tag in state.selectedTags)
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        else Color.Transparent
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Botón publicar
        AppButton(
            texto = "Publicar reseña",
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally),
            onClick = onSaveClick,
            height = 70.dp,
            fontSize = 20.sp
        )
    }
}
