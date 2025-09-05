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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.R
import com.example.myapplication.utils.PasswordField
import com.example.myapplication.utils.AppButton
import com.example.myapplication.utils.LogoApp
import com.example.myapplication.utils.CustomTextField
import com.example.myapplication.utils.DateBirthField
import android.util.Log

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
    state: RegisterState,
    onNameChange: (String) -> Unit,
    onUsuarioChange: (String) -> Unit,
    onFechaNacimientoChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        CustomTextField(
            value = state.name,
            onValueChange = onNameChange,
            label = "Nombre",
            modifier = Modifier.fillMaxWidth()
        )

        CustomTextField(
            value = state.usuario,
            onValueChange = onUsuarioChange,
            label = "Usuario",
            modifier = Modifier.fillMaxWidth()
        )

        DateBirthField(
            value = state.fechaNacimiento,
            onValueChange = onFechaNacimientoChange,
            modifier = Modifier.fillMaxWidth()
        )

        CustomTextField(
            value = state.email,
            onValueChange = onEmailChange,
            label = "Correo electrónico",
            modifier = Modifier.fillMaxWidth()
        )

        PasswordField(
            value = state.password,
            onValueChange = onPasswordChange,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
@Composable
fun BodyRegisterScreen(
    registerViewModel: RegisterViewModel,
    onRegisterSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by registerViewModel.uiState.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        MensajeBienvenida(
            stringResource(R.string.nocta),
            modifier = Modifier.padding(bottom = 10.dp)
        )

        FormularioRegistro(
            state = state,
            onNameChange = { registerViewModel.updateName(it) },
            onUsuarioChange = { registerViewModel.updateUsuario(it) },
            onFechaNacimientoChange = { registerViewModel.updateFechaNacimiento(it) },
            onEmailChange = { registerViewModel.updateEmail(it) },
            onPasswordChange = { registerViewModel.updatePassword(it) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        AppButton(
            texto = stringResource(R.string.crear_cuenta),
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                registerViewModel.register(
                    onSuccess = {
                        Log.d("Register", "Usuario registrado: ${state.email} / ${state.usuario}")
                        onRegisterSuccess()
                    },
                    onError = { message ->
                        Log.e("Register", "Error al registrar: $message")
                    }
                )
            }
        )
    }
}


@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    registerViewModel: RegisterViewModel = hiltViewModel(),
    onRegisterSuccess: () -> Unit
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
                registerViewModel = registerViewModel,
                onRegisterSuccess = onRegisterSuccess
            )
        }
    }
}


