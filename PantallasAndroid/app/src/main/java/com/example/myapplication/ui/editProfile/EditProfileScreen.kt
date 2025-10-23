package com.example.myapplication.ui.profile

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.utils.AppButton
import com.example.myapplication.utils.ProfileAsyncImage
import com.example.myapplication.utils.CustomTextField

@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: EditProfileViewModel = hiltViewModel(),
) {
    // Estado del ViewModel
    val state by viewModel.uiState.collectAsState()

    // Launcher para seleccionar imagen
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            Log.d("EditProfile", "Selected image Uri: $it")
            viewModel.uploadImageToFirebase(it)
        } ?: Log.d("EditProfile", "No image selected")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 110.dp)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ProfileAsyncImage(
            profileImage = state.profilePicUrl.orEmpty(),
            size = 120
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { launcher.launch("image/*") },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "Change Picture",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        CustomTextField(
            value = state.name,
            onValueChange = { viewModel.updateName(it) },
            label = "Nombre",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        CustomTextField(
            value = state.usuario,
            onValueChange = { viewModel.updateUsuario(it) },
            label = "Usuario",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        CustomTextField(
            value = state.birthdate,
            onValueChange = { viewModel.updateFechaNacimiento(it) },
            label = "Fecha de nacimiento",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        CustomTextField(
            value = state.email,
            onValueChange = { viewModel.updateEmail(it) },
            label = "Email",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Mostrar texto de loading si el estado lo indica.
        val isLoading = state.isLoading

        AppButton(
            texto = if (isLoading) "Guardando..." else "Guardar cambios",
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                // No mostramos Snackbar aquí — el Scaffold superior debe observar viewModel.uiState
                viewModel.saveProfile(
                    onSuccess = { /* El contenedor superior puede reaccionar a uiState.isLoading / errorMessage */ },
                    onError = { /* El contenedor superior mostrará el error desde uiState.errorMessage */ }
                )
            },
            height = 60.dp,
            fontSize = 18.sp
        )
    }
}
