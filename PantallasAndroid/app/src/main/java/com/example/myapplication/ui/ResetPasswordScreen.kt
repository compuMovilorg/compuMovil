package com.example.myapplication.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.utils.AppButton
import com.example.myapplication.utils.LogoApp

@Composable
fun ResetPasswordBody(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(R.drawable.fondo),
            contentDescription = "fondo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Contenido centrado
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
                .padding(24.dp)
        ) {
            // Logo
            LogoApp(modifier = Modifier.padding(100.dp))

            // Campo de correo
            AppButton(
                texto = stringResource(R.string.email),
                modifier = Modifier.padding(10.dp),
                onClick = { /* Crear cuenta */ }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de restablecer
            AppButton(
                texto = stringResource(R.string.reset_password),
                modifier = Modifier.padding(10.dp),
                onClick = { /* Crear cuenta */ }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Texto de instrucción
            Text(
                text = "Verifica tu correo electrónico para restablecer tu contraseña",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ResetPasswordScreen(modifier: Modifier = Modifier) {
    ResetPasswordBody(modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun ResetPasswordScreenPreview() {
    ResetPasswordScreen()
}
