package com.example.myapplication.ui.log

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.R
import com.example.myapplication.utils.PasswordField
import com.example.myapplication.utils.CustomTextField
import com.example.myapplication.utils.AppButton

@Composable
fun LoginBody(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel,
    onLoginClick: (String, String) -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .padding(bottom = 175.dp)
                .size(200.dp)
        )

        // Título
        Text(
            text = "Bienvenido a Nocta",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Email
        CustomTextField(
            value = email,
            onValueChange = { viewModel.updateEmail(it) },
            label = "Correo o Usuario",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo Contraseña
        PasswordField(
            value = password,
            onValueChange = { viewModel.updatePassword(it) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón login
        AppButton(
            texto = stringResource(R.string.iniciar_sesion),
            modifier = Modifier.fillMaxWidth(),
            onClick = { onLoginClick(email, password) }
        )

        // Link "Olvidaste tu contraseña"
        Text(
            text = "¿Olvidaste tu contraseña?",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onForgotPasswordClick()
                }
        )
    }
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel(),
    onLoginClick: (String, String) -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fondolog),
            contentDescription = "Fondo log",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Contenido de login sobre el fondo
        LoginBody(
            modifier = modifier.fillMaxSize(),
            viewModel = viewModel,
            onLoginClick = onLoginClick,
            onForgotPasswordClick = onForgotPasswordClick
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginBody() {
    LoginScreen(
        onLoginClick = { _, _ -> },
        onForgotPasswordClick = {}
    )
}
