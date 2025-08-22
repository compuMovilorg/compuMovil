package com.example.myapplication.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.GastroBar
import com.example.myapplication.data.local.LocalGastroBarProvider
import com.example.myapplication.utils.SearchBarField
import com.example.myapplication.utils.GastroBarGrid

@Composable
fun BodySearchScreen(
    modifier: Modifier = Modifier,
    gastroBars: List<GastroBar>
) {
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {

        SearchBarField(
            modifier = Modifier.padding(top = 16.dp),
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = "Buscar..."
        )

        Spacer(modifier = Modifier.height(8.dp))


        val filteredBars = if (searchQuery.isBlank()) {
            gastroBars
        } else {
            gastroBars.filter { it.name.contains(searchQuery, ignoreCase = true) }
        }

        GastroBarGrid(
            gastroBars = filteredBars,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    gastroBars: List<GastroBar>
) {
    BodySearchScreen(gastroBars = gastroBars)
}

@Composable
@Preview(showBackground = true)
fun BodySearchScreenPreview() {
    SearchScreen(
        gastroBars = LocalGastroBarProvider.gastroBars
    )
}

@Composable
@Preview(showBackground = true)
fun SearchScreenPreview() {
    SearchScreen(
        gastroBars = LocalGastroBarProvider.gastroBars
    )
}