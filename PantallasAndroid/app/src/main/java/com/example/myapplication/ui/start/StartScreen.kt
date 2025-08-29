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
                .padding(bottom = 175.dp)
                .size(200.dp)
        )

        AppButton(
            texto = stringResource(R.string.iniciar_sesion),
            modifier = Modifier.padding(10.dp),
            onClick = onLoginClick
        )

        AppButton(
            texto = stringResource(R.string.crear_cuenta),
            modifier = Modifier.padding(10.dp),
            onClick = onRegisterClick
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
    val loginPressed by viewModel.loginPressed.collectAsState()
    val registerPressed by viewModel.registerPressed.collectAsState()

    LaunchedEffect(loginPressed) {
        if (loginPressed) {
            onNavigateLogin()
            viewModel.resetState()
        }
    }

    LaunchedEffect(registerPressed) {
        if (registerPressed) {
            onNavigateRegister()
            viewModel.resetState()
        }
    }

    Box(
        modifier = modifier
    ) {
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
            Spacer(modifier = Modifier.weight(1F))
            BodyStartScreen(
                onLoginClick = { viewModel.onLoginPressed() },
                onRegisterClick = { viewModel.onRegisterPressed() }
            )
            Spacer(modifier = Modifier.weight(1F))
            Text(
                "Nocta all rights reserved",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
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
