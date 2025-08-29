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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.utils.StarRating

@Composable
fun CreateScreenBody(
    onSaveClick: (String, String, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var placeName by remember { mutableStateOf(TextFieldValue("")) }
    var reviewText by remember { mutableStateOf(TextFieldValue("")) }
    var rating by remember { mutableStateOf(0f) }
    var selectedImages by remember { mutableStateOf(listOf<Int>()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 110.dp)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // Nombre del lugar
        OutlinedTextField(
            value = placeName,
            onValueChange = { placeName = it },
            label = { Text("Nombre del lugar") },
            placeholder = { Text("Buscar o seleccionar un gastrobar") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Rating
        Text(text = "Calificar", style = MaterialTheme.typography.bodyMedium)
        StarRating(
            rating = rating,
            onRatingChanged = { newRating -> rating = newRating.toFloat() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Fotos
        Text(text = "Fotos")
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                        .clickable {
                            // Aquí iría lógica de abrir galería
                            selectedImages =
                                selectedImages + R.drawable.gastrobarimg1
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text("+")
                }
            }
            items(selectedImages.size) { index ->
                Image(
                    painter = painterResource(id = selectedImages[index]),
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
            value = reviewText,
            onValueChange = { reviewText = it },
            label = { Text("Excribe tu reseña") },
            placeholder = { Text("Escribe sobre el lugar...") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))


        Text(text = "Etiquetas")
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Cocteles", "Ambiente", "Servicio", "Precio").forEach { tag ->
                AssistChip(
                    onClick = { /* TODO seleccionar tag */ },
                    label = { Text(tag) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón publicar
        Button(
            onClick = {
                onSaveClick(placeName.text, reviewText.text, rating)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Publicar reseña")
        }
    }
}

@Composable
fun CreateScreen(
    onSaveClick: (String, String, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    CreateScreenBody(
        onSaveClick = onSaveClick,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCreateScreen() {
    CreateScreen(
        onSaveClick = { _, _, _ -> }
    )
}
