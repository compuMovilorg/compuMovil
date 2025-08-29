package com.example.myapplication.ui.search

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.GastroBar
import com.example.myapplication.utils.SearchBarField
import com.example.myapplication.utils.GastroBarGrid

@Composable
fun BodySearchScreen(
    viewModel: SearchViewModel,
    onGastroBarClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {

        SearchBarField(
            modifier = Modifier.padding(top = 16.dp),
            value = state.searchQuery,
            onValueChange = { viewModel.updateSearchQuery(it) },
            placeholder = "Buscar..."
        )

        Spacer(modifier = Modifier.height(8.dp))

        GastroBarGrid(
            gastroBars = viewModel.filteredBars,
            modifier = Modifier.fillMaxSize(),
            onGridItemClick = { bar -> onGastroBarClick(bar.id) }
        )
    }
}

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    gastroBars: List<GastroBar>,
    viewModel: SearchViewModel = viewModel(),
    onGastroBarClick: (Int) -> Unit
) {
    viewModel.setGastroBars(gastroBars)

    BodySearchScreen(
        viewModel = viewModel,
        onGastroBarClick = onGastroBarClick,
        modifier = modifier
    )
}

@Composable
@Preview(showBackground = true)
fun BodySearchScreenPreview() {
    val vm: SearchViewModel = viewModel()
    BodySearchScreen(
        viewModel = vm,
        onGastroBarClick = {}
    )
}

@Composable
@Preview(showBackground = true)
fun SearchScreenPreview() {
    val vm: SearchViewModel = viewModel()
    SearchScreen(
        gastroBars = com.example.myapplication.data.local.LocalGastroBarProvider.gastroBars,
        viewModel = vm,
        onGastroBarClick = {}
    )
}
