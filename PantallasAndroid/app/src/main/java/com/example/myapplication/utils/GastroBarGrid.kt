import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myapplication.data.GastroBar

private const val TAG_GRID = "GastroBarGrid"

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
        itemsIndexed(
            items = gastroBars,
            key = { idx, bar -> (bar.id?.takeIf { it.isNotBlank() } ?: "idx-$idx") + "-${bar.name.hashCode()}" }
        ) { index, bar ->
            AsyncImage(
                model = bar.imagePlace?.takeIf { it.isNotBlank() },
                contentDescription = bar.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(1f)
                    .border(1.dp, MaterialTheme.colorScheme.onSurface, MaterialTheme.shapes.small)
                    .clickable {
                        Log.d(TAG_GRID, "CLICK index=$index id=${bar.id} name=${bar.name}")
                        if (bar.id.isNullOrBlank()) {
                            Log.e(TAG_GRID, "Bloqueado: id nulo/vacío para name=${bar.name}")
                            return@clickable
                        }
                        onGridItemClick(bar)
                    }
            )
        }
    }
}
