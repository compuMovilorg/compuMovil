package com.example.myapplication.utils

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun DateBirthField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Fecha de nacimiento (AAAA/MM/DD)",
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = { input ->
            // Permite escribir con o sin '/'. Auto-formatea a AAAA/MM/DD
            val digits = input.filter { it.isDigit() }.take(8)
            val formatted = when {
                digits.length <= 4 -> digits
                digits.length <= 6 -> digits.substring(0, 4) + "/" + digits.substring(4)
                else -> digits.substring(0, 4) + "/" + digits.substring(4, 6) + "/" + digits.substring(6)
            }
            onValueChange(formatted)
        },
        label = { Text(label) },
        modifier = modifier,               // ← aquí se respeta el testTag externo
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onAny = { onImeAction?.invoke() }
        )
    )
}
