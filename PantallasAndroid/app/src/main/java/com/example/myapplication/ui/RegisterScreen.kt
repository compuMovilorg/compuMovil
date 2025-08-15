package com.example.myapplication.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.components.PasswordField
import com.example.myapplication.utils.AppButton
import com.example.myapplication.utils.EmailField
import com.example.myapplication.utils.LogoApp
import com.example.myapplication.utils.NameField
import com.example.myapplication.utils.UserField
import com.example.myapplication.utils.DateBirthField

@Composable
fun MensajeBienvenida(
    nombre: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = "Bienvenido a $nombre",
        fontSize = 20.sp,
        style = MaterialTheme.typography.headlineMedium,
        color = Color.Black,
        modifier = modifier
    )
}

@Composable
fun FormularioRegistro(
    name: String,
    onNameChange: (String) -> Unit,
    usuario: String,
    onUsuarioChange: (String) -> Unit,
    fechaNacimiento: String,
    onFechaNacimientoChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        NameField(
            value = name,
            onValueChange = onNameChange,
            modifier = Modifier.fillMaxWidth()
        )

        UserField(
            value = usuario,
            onValueChange = onUsuarioChange,
            modifier = Modifier.fillMaxWidth()
        )

        DateBirthField(
            value = fechaNacimiento,
            onValueChange = onFechaNacimientoChange,
            modifier = Modifier.fillMaxWidth()
        )

        EmailField(
            value = email,
            onValueChange = onEmailChange,
            modifier = Modifier.fillMaxWidth()
        )

        PasswordField(
            value = password,
            onValueChange = onPasswordChange,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun BodyRegisterScreen(
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxHeight()
            .background(Color.White)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            MensajeBienvenida(
                stringResource(R.string.nocta),
                modifier = Modifier.padding(bottom = 10.dp)
            )

            FormularioRegistro(
                name = name,
                onNameChange = { name = it },
                usuario = usuario,
                onUsuarioChange = { usuario = it },
                fechaNacimiento = fechaNacimiento,
                onFechaNacimientoChange = { fechaNacimiento = it },
                email = email,
                onEmailChange = { email = it },
                password = password,
                onPasswordChange = { password = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppButton(
                texto = stringResource(R.string.crear_cuenta),
                modifier = Modifier.fillMaxWidth(),
                onClick = { /* Acción crear cuenta */ }
            )
        }
    }
}
@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Fondo con imagen
        Image(
            painter = painterResource(R.drawable.fondosign),
            contentDescription = "fondo sign",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LogoApp(
                modifier = Modifier
                    .padding(top = 50.dp)
                    .size(150.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            BodyRegisterScreen(
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun BodyRegisterScreenPreview() {
    BodyRegisterScreen()
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen()
}
