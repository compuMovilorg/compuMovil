package com.example.myapplication.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.GastroBar
import com.example.myapplication.data.local.LocalGastroBarProvider

@Composable
fun GastroBarGrid(
    gastroBars: List<GastroBar>,
    modifier: Modifier = Modifier,
    onGridItemClick: (GastroBar) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        contentPadding = PaddingValues(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(gastroBars) { bar ->
            Image(
                painter = painterResource(id = bar.imagePlace),
                contentDescription = bar.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(1f)
                    .border(1.dp, MaterialTheme.colorScheme.onSurface)
                    .clickable { onGridItemClick(bar) }
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun GastroBarGridPreview() {
    GastroBarGrid(
        gastroBars = LocalGastroBarProvider.gastroBars,
        onGridItemClick = {}
    )
}


