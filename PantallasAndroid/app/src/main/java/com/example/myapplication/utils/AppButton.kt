package com.example.myapplication.utils

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun AppButton(
    texto: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    height: Dp = 50.dp,           // altura por defecto
    fontSize: TextUnit = 16.sp     // tamaño de texto por defecto
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = modifier
            .padding(10.dp)
            .height(height)   // altura personalizada
    ) {
        Text(
            text = texto,
            fontSize = fontSize
        )
    }
}
