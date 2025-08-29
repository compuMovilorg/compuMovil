package com.example.myapplication.ui.resetPassword

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.R
import com.example.myapplication.utils.AppButton
import com.example.myapplication.utils.CustomTextField
import com.example.myapplication.utils.LogoApp

@Composable
fun ResetPasswordBody(
    modifier: Modifier = Modifier,
    viewModel: ResetPasswordViewModel = viewModel(),
    onLogInClick: () -> Unit
) {
   val state by viewModel.uiState.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        // Imagen de fondo
        Image(
            painter = painterResource(R.drawable.fondo),
            contentDescription = "fondo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Logo
            LogoApp(modifier = Modifier.padding(bottom = 60.dp))

            Text(
                text = "¿Olvido su contraseña?",
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Ingrese su correo para recibir el link de restablecimiento",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            CustomTextField(
                value = state.email,
                onValueChange = { viewModel.updateEmail(it) },
                label = "Correo",
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppButton(
                texto = "Restablecer contraseña",
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.sendResetPassword() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿Se acuerda de su contraseña? ",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                )
                Text(
                    text = "Iniciar sesion",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.clickable { onLogInClick() }
                )
            }
        }
    }
}


@Composable
fun ResetPasswordScreen(
    modifier: Modifier = Modifier,
    onLogInClick: () -> Unit = {}
) {
    ResetPasswordBody(modifier = modifier, onLogInClick = onLogInClick)
}

@Preview(showBackground = true)
@Composable
fun ResetPasswordScreenPreview() {
    ResetPasswordScreen()
}
