package com.example.myapplication.ui

import androidx.compose.foundation.Image
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.utils.PasswordField
import com.example.myapplication.utils.CustomTextField
import com.example.myapplication.utils.AppButton

@Composable
fun LoginBody(
    modifier: Modifier = Modifier,
    onLoginClick: (String, String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
            contentDescription = null,
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
            onValueChange = { email = it },
            label = "Correo o Usuario",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo Contraseña
        PasswordField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón login
        AppButton(
            texto = stringResource(R.string.iniciar_sesion),
            modifier = Modifier.padding(bottom = 10.dp),
            onClick = { onLoginClick(email, password) }
        )
    }
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginClick: (String, String) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondolog),
            contentDescription = "Fondo log",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        LoginBody(
            modifier = Modifier.fillMaxSize(),
            onLoginClick = onLoginClick
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginBody() {
    LoginBody { _, _ -> }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen { _, _ -> }
}
