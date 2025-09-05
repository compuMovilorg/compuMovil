package com.example.myapplication.ui.log

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
    viewModel: LoginViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

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
            value = state.email,
            onValueChange = { viewModel.updateEmail(it) },
            label = "Correo o Usuario",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo Contraseña
        PasswordField(
            value = state.password,
            onValueChange = { viewModel.updatePassword(it) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón login
        AppButton(
            texto = stringResource(R.string.iniciar_sesion),
            modifier = Modifier.fillMaxWidth(),
            onClick = { viewModel.onLogin() }
        )

        // Link "Olvidaste tu contraseña"
        Text(
            text = "¿Olvidaste tu contraseña?",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable {
                    viewModel.onForgotPassword()
                }
        )
    }
}


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel(),
    onLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onError: (String) -> Unit = {}
) {
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                LoginEvent.NavigateToHome -> {
                    onLoginClick()
                    viewModel.clearEvent()
                }
                LoginEvent.NavigateToForgotPassword -> {
                    onForgotPasswordClick()
                    viewModel.clearEvent()
                }
                is LoginEvent.ShowError -> {
                    onError(event.message)
                    viewModel.clearEvent()
                }
                null -> Unit
            }
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fondolog),
            contentDescription = "Fondo log",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        LoginBody(
            modifier = modifier.fillMaxSize(),
            viewModel = viewModel
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginBody() {
    LoginScreen(
        onLoginClick = {},
        onForgotPasswordClick = {},
        onError = {}
    )
}
