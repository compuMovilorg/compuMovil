package com.example.myapplication.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.myapplication.data.GastroBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GastroBarPicker(
    items: List<GastroBar>,
    value: String,
    onValueChange: (String) -> Unit,
    onSelect: (GastroBar) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Nombre del lugar",
    isLoading: Boolean = false,
    enabled: Boolean = true,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    // Filtro local (hasta 10 resultados)
    val suggestions = remember(value, items) {
        val q = value.trim()
        val base = if (q.isEmpty()) items else items.filter {
            it.name.contains(q, ignoreCase = true) ||
                    (it.address?.contains(q, ignoreCase = true) == true)
        }
        base.take(10)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (enabled) expanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
                if (!expanded && enabled) expanded = true
            },
            label = { Text(label) },
            singleLine = true,
            enabled = enabled,
            trailingIcon = {
                when {
                    isLoading -> CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.padding(end = 6.dp))
                    else -> Icon(
                        imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (isLoading) {
                DropdownMenuItem(
                    text = { Text("Cargando…") },
                    onClick = { /* no-op */ },
                    enabled = false
                )
            } else if (suggestions.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("Sin coincidencias") },
                    onClick = { /* no-op */ },
                    enabled = false
                )
            } else {
                suggestions.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(item.name, fontWeight = FontWeight.SemiBold)
                                if (!item.address.isNullOrBlank()) {
                                    Text(item.address!!, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        },
                        onClick = {
                            onValueChange(item.name)     // escribe el nombre en el campo
                            onSelect(item)               // devuélvelo (usa item.id en tu VM)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
