package com.example.myapplication.ui.register

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.R
import com.example.myapplication.utils.AppButton
import com.example.myapplication.utils.CustomTextField
import com.example.myapplication.utils.DateBirthField
import com.example.myapplication.utils.LogoApp
import com.example.myapplication.utils.PasswordField

// ---------- UI ----------

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    registerViewModel: RegisterViewModel = hiltViewModel(),
    onRegisterSuccess: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        // Fondo con imagen (igual que tu diseño)
        Image(
            painter = painterResource(R.drawable.fondosign),
            contentDescription = "fondo sign",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Contenido
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo arriba
            LogoApp(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .size(150.dp)
            )

            Spacer(Modifier.height(20.dp))

            // Tarjeta blanca con el formulario (con scroll por si el teclado cubre)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                    .shadow(elevation = 8.dp, shape = MaterialTheme.shapes.large),
                shape = MaterialTheme.shapes.large
            ) {
                BodyRegisterScreen(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                        .verticalScroll(rememberScrollState())
                        .imePadding(),
                    registerViewModel = registerViewModel,
                    onRegisterSuccess = onRegisterSuccess
                )
            }
        }
    }
}

@Composable
private fun BodyRegisterScreen(
    registerViewModel: RegisterViewModel,
    onRegisterSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by registerViewModel.uiState.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
    ) {
        MensajeBienvenida(
            nombre = stringResource(R.string.nocta),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        FormularioRegistro(
            state = state,
            onNameChange = registerViewModel::updateName,
            onUsuarioChange = registerViewModel::updateUsuario,
            onFechaNacimientoChange = registerViewModel::updateFechaNacimiento,
            onEmailChange = registerViewModel::updateEmail,
            onPasswordChange = registerViewModel::updatePassword,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        AppButton(
            texto = stringResource(R.string.crear_cuenta),
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                // Log de verificación ANTES de enviar a Firestore
                Log.d(
                    "Register",
                    "DATA -> name='${state.name}', user='${state.usuario}', birth='${state.fechaNacimiento}', email='${state.email}'"
                )
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
private fun MensajeBienvenida(
    nombre: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = "Bienvenido a $nombre",
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        style = MaterialTheme.typography.headlineMedium,
        modifier = modifier
    )
}

@Composable
private fun FormularioRegistro(
    state: RegisterState,
    onNameChange: (String) -> Unit,
    onUsuarioChange: (String) -> Unit,
    onFechaNacimientoChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        // Nombre
        CustomTextField(
            value = state.name,
            onValueChange = onNameChange,
            label = "Nombre",
            modifier = Modifier.fillMaxWidth()
        )

        // Usuario
        CustomTextField(
            value = state.usuario,
            onValueChange = onUsuarioChange,
            label = "Usuario",
            modifier = Modifier.fillMaxWidth()
        )

        // Fecha de nacimiento (con tu componente)
        DateBirthField(
            value = state.fechaNacimiento,
            onValueChange = onFechaNacimientoChange,
            modifier = Modifier.fillMaxWidth()
        )

        // Correo
        CustomTextField(
            value = state.email,
            onValueChange = onEmailChange,
            label = "Correo electrónico",
            modifier = Modifier.fillMaxWidth()
        )

        // Contraseña (usa tu PasswordField para mantener estilo)
        PasswordField(
            value = state.password,
            onValueChange = onPasswordChange,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
