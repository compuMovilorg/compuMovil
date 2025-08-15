package com.example.myapplication.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun EmailField(
    value: String = "",
    modifier: Modifier,
    onValueChange: (String) -> Unit
) {
    val text = remember { mutableStateOf(value) }
    OutlinedTextField(
        value = text.value,
        onValueChange = {
            text.value = it
            onValueChange(it)
        },
        label = { Text("Correo electronico") },
        modifier = Modifier.fillMaxWidth()
    )
}