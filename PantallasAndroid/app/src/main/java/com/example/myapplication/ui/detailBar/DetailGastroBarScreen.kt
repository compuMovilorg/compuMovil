package com.example.myapplication.ui.detailBar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.data.GastroBar
import com.example.myapplication.utils.StarRating
import com.example.myapplication.utils.AppButton

@Composable
fun DetailGastroBarScreen(
    gastroBar: GastroBar,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        HeaderImage(imageRes = gastroBar.imagePlace)

        PlaceInfoSection(gastroBar = gastroBar)

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppButton(
                texto = "Calificar y Escribir reseña",
                modifier = Modifier.fillMaxWidth(),
                onClick = { /* Lógica para calificar */ }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = { /* Lógica para ver reseñas */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Ver reseñas")
            }
        }
    }
}

@Composable
fun HeaderImage(imageRes: Int) {
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = "Gastrobar Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    )
}

@Composable
fun PlaceInfoSection(gastroBar: GastroBar) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = gastroBar.name,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            fontSize = 24.sp
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            StarRating(rating = gastroBar.rating)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${gastroBar.rating} (${gastroBar.reviewCount} reseñas)",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        InfoRow(
            icon = Icons.Default.LocationOn,
            text = gastroBar.address
        )
        Spacer(modifier = Modifier.height(8.dp))
        InfoRow(
            icon = Icons.Default.Schedule,
            text = gastroBar.hours
        )
        Spacer(modifier = Modifier.height(8.dp))
        InfoRow(
            icon = Icons.Default.Lightbulb,
            text = gastroBar.cuisine
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = gastroBar.description,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun InfoRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}

@Preview(showBackground = true)
@Composable
fun DetailGastroBarScreenPreview() {
    val sampleGastroBar = GastroBar(
        id = 1,
        imagePlace = R.drawable.gastrobarimg1,
        name = "Santa Juana Gastrobar",
        rating = 4.5f,
        reviewCount = 30,
        address = "Calle 71 #11-51",
        hours = "Lunes a Sábado de 8:00 am. a 11:00 pm\nDomingos y festivos, de 7:30 a.m. a 11:00 p.m",
        cuisine = "Fusión Colombiana",
        description = "Santa Juana es un gastrobar en Quinta Camacho que fusiona arte y gastronomía. Ofrece una experiencia única con cocina latina, coctelería artesanal y un ambiente lleno de diseño e inspiración ancestral."
    )

    MaterialTheme {
        DetailGastroBarScreen(
            gastroBar = sampleGastroBar,
            modifier = Modifier.fillMaxSize()
        )
    }
}
