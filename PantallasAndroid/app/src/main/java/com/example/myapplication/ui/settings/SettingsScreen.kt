package com.example.myapplication.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.utils.LogOutButton
import com.example.myapplication.utils.SettingsOption
import com.example.myapplication.ui.settings.SettingsViewModel

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel,
    onLogoutClick: () -> Unit = {}
) {
    val uiState = viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 100.dp)
    ) {

        Text(
            text = stringResource(R.string.configuracion),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        SettingsOption(
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
            title = stringResource(R.string.informacionpersonal),
            onClick = { /* Navegar */ }
        )

        Divider()

        SettingsOption(
            icon = { Icon(Icons.Default.Lock, contentDescription = null) },
            title = stringResource(R.string.privacidad),
            onClick = { /* Navegar */ }
        )

        Divider()

        SettingsOption(
            icon = { Icon(Icons.Default.Block, contentDescription = null) },
            title = stringResource(R.string.bloqueados),
            onClick = { /* Navegar */ }
        )

        Spacer(modifier = Modifier.padding(8.dp))

        LogOutButton(
            onLogoutClick = {
                viewModel.logout()
                onLogoutClick()
            },
            modifier = Modifier.fillMaxWidth()
        )
    }

    if (uiState.value.isLoggedOut) {
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewSettingsScreen() {
//    MaterialTheme {
//        SettingsScreen(
//            uiState = SettingsUiState(isLoggedOut = false),
//            onLogoutClick = {}
//        )
//    }
//}


