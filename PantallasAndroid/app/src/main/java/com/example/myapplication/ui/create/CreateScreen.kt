package com.example.myapplication.ui.create

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
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
        onSelectGastroBar = viewModel::onSelectGastroBar,
        onReviewTextChange = viewModel::onReviewTextChanged,
        onRatingChange = viewModel::onRatingChanged,
        onSelectImage = viewModel::onSelectImage,
        onToggleTag = viewModel::onToggleTag,
        onSaveClick = { viewModel.createReview() },
        modifier = modifier
    )
}

@Composable
fun CreateScreenBody(
    state: CreateState,
    onPlaceNameChange: (String) -> Unit,
    onSelectGastroBar: (String, String) -> Unit,
    onReviewTextChange: (String) -> Unit,
    onRatingChange: (Float) -> Unit,
    onSelectImage: (Uri) -> Unit,
    onToggleTag: (String) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { onSelectImage(it) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 110.dp)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        GastroBarPicker(
            items = state.gastrobares,
            value = state.placeName,
            onValueChange = onPlaceNameChange,
            onSelect = { selected ->
                onSelectGastroBar(selected.id, selected.name ?: "")
            },
            isLoading = state.isLoadingGastrobares,
            label = "Nombre del lugar",
            enabled = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Calificar", style = MaterialTheme.typography.bodyMedium)
        StarRating(rating = state.rating, onRatingChanged = { onRatingChange(it.toFloat()) })

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Foto del lugar")
        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                        .clickable { imagePicker.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    Text("+")
                }
            }

            state.selectedImageUri?.let { uri ->
                item {
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = "Imagen seleccionada",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(90.dp)
                            .background(Color.LightGray, RoundedCornerShape(8.dp))
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = state.reviewText,
            onValueChange = onReviewTextChange,
            label = { Text("Escribe tu reseña") },
            modifier = Modifier.fillMaxWidth().height(120.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

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

        AppButton(
            texto = if (state.isSubmitting) "Publicando..." else "Publicar reseña",
            modifier = Modifier.fillMaxWidth(0.9f).align(Alignment.CenterHorizontally),
            onClick = onSaveClick,
            height = 70.dp,
            fontSize = 20.sp
        )
    }
}
