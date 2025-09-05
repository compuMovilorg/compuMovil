package com.example.myapplication.ui.register

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.R
import com.example.myapplication.utils.PasswordField
import com.example.myapplication.utils.AppButton
import com.example.myapplication.utils.LogoApp
import com.example.myapplication.utils.CustomTextField
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
        CustomTextField(
            value = name,
            onValueChange = onNameChange,
            label = "Nombre",
            modifier = Modifier.fillMaxWidth()
        )

        CustomTextField(
            value = usuario,
            onValueChange = onUsuarioChange,
            label = "Usuario",
            modifier = Modifier.fillMaxWidth()
        )

        DateBirthField(
            value = fechaNacimiento,
            onValueChange = onFechaNacimientoChange,
            modifier = Modifier.fillMaxWidth()
        )

        CustomTextField(
            value = email,
            onValueChange = onEmailChange,
            label = "Correo electrónico",
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
    registerViewModel: RegisterViewModel,

    modifier: Modifier = Modifier
) {
    val state by registerViewModel.uiState.collectAsState()
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
                name = state.name,
                onNameChange = { registerViewModel.updateName(it) },
                usuario = state.usuario,
                onUsuarioChange = { registerViewModel.updateUsuario(it) },
                fechaNacimiento = state.fechaNacimiento,
                onFechaNacimientoChange = { registerViewModel.updateFechaNacimiento(it) },
                email = state.email,
                onEmailChange = { registerViewModel.updateEmail(it) },
                password = state.password,
                onPasswordChange = { registerViewModel.updatePassword(it) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            AppButton(
                texto = stringResource(R.string.crear_cuenta),
                modifier = Modifier.fillMaxWidth(),
                onClick = { registerViewModel.register() }
            )
            state.result?.let { result ->
                Spacer(modifier = Modifier.height(8.dp))
                when (result) {
                    is RegisterResult.Success -> Text("Registro exitoso", color = Color.Green)
                    is RegisterResult.Error -> Text(result.message, color = Color.Red)
                }
            }
        }
    }
}

@Composable
fun RegisterScreen(

    modifier: Modifier = Modifier,
    registerViewModel: RegisterViewModel = viewModel()
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

                modifier = Modifier.fillMaxWidth(),
                registerViewModel = registerViewModel
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BodyRegisterScreenPreview() {
    val vm: RegisterViewModel = viewModel()
    BodyRegisterScreen(
        registerViewModel = vm
    )
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(
        registerViewModel = viewModel()
    )
}
