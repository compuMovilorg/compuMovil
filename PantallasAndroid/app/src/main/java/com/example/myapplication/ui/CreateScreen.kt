package com.example.myapplication.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    var rating: Float by remember { mutableStateOf(0f) }
    var selectedImageRes by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Crear Reseña",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Nombre del lugar
        OutlinedTextField(
            value = placeName,
            onValueChange = { placeName = it },
            label = { Text("Nombre del lugar") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Reseña
        OutlinedTextField(
            value = reviewText,
            onValueChange = { reviewText = it },
            label = { Text("Escribe tu reseña") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Rating interactivo
        Text(text = "Calificación:")
        StarRating(
            rating = rating,
            onRatingChanged = { newRating -> rating = newRating.toFloat() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Imagen opcional
        Text(text = "Añadir foto (opcional):")
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    selectedImageRes = R.drawable.gastrobarimg1
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Añadir imagen")
            }

            selectedImageRes?.let { resId ->
                Image(
                    painter = painterResource(id = resId),
                    contentDescription = "Imagen seleccionada",
                    modifier = Modifier
                        .size(64.dp)
                        .padding(4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Guardar reseña
        Button(
            onClick = {
                onSaveClick(placeName.text, reviewText.text,rating)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Reseña")
        }
    }
}

@Composable
fun CreateScreen(
    onSaveClick: (String, String, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    CreateScreenBody(
        onSaveClick = onSaveClick as (String, String, Float) -> Unit,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCreateScreen() {
    CreateScreenBody(
        onSaveClick = { _, _, _ -> }
    )
}
