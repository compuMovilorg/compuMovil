package com.example.myapplication.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.utils.LogoApp
import com.example.myapplication.utils.AppButton

@Composable
fun BodyHomeScreen(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        LogoApp(
            modifier = Modifier
                .padding(bottom = 175.dp)
                .size(200.dp)
        )

        AppButton(
            texto = stringResource(R.string.iniciar_sesion),
            modifier = Modifier.padding(10.dp),
            onClick = { /* Iniciar Sesion */ }

        )
        AppButton(
            texto = stringResource(R.string.crear_cuenta),
            modifier = Modifier.padding(10.dp),
            onClick = { /* Crear cuenta */ }
        )
    }
}

@Composable
@Preview(showBackground = true)
fun BodyHomeScreenPreview() {
    BodyHomeScreen()
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(R.drawable.fondo),
            contentDescription = "fondo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.weight(1F))
            BodyHomeScreen()
            Spacer(modifier = Modifier.weight(1F))
            Text(
                "Nocta all rights reserved",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun HomeScreenPreview() {
    HomeScreen()
}
