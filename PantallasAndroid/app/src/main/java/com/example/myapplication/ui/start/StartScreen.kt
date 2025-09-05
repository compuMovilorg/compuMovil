package com.example.myapplication.ui.start

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.R
import com.example.myapplication.utils.LogoApp
import com.example.myapplication.utils.AppButton

@Composable
fun BodyStartScreen(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        LogoApp(
            modifier = Modifier
                .padding(bottom = 120.dp) // un poco menos para dar más espacio a los botones
                .size(200.dp)
        )

        AppButton(
            texto = stringResource(R.string.iniciar_sesion),
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(0.7f),
            onClick = onLoginClick,
            height = 60.dp, // más alto
            fontSize = 22.sp
        )

        AppButton(
            texto = stringResource(R.string.crear_cuenta),
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(0.7f),
            onClick = onRegisterClick,
            height = 60.dp,
            fontSize = 22.sp
        )
    }
}


@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    viewModel: StartViewModel = viewModel(),
    onNavigateLogin: () -> Unit,
    onNavigateRegister: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.loginPressed) {
        if (uiState.loginPressed) {
            onNavigateLogin()
            viewModel.resetState()
        }
    }

    LaunchedEffect(uiState.registerPressed) {
        if (uiState.registerPressed) {
            onNavigateRegister()
            viewModel.resetState()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
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
            Spacer(modifier = Modifier.weight(1f))
            BodyStartScreen(
                onLoginClick = { viewModel.onLoginPressed() },
                onRegisterClick = { viewModel.onRegisterPressed() }
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                "Nocta all rights reserved",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BodyStartScreenPreview() {
    BodyStartScreen(
        onLoginClick = {},
        onRegisterClick = {}
    )
}
