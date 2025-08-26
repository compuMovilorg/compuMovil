package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.utils.LogoApp

@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier
) {
    var newFollowers by remember { mutableStateOf(true) }
    var comments by remember { mutableStateOf(false) }
    var likes by remember { mutableStateOf(true) }
    var recommendations by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Simple TopAppBar equivalent without Scaffold
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp), // Manual padding to clear the status bar
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        // Header section (now a simple white box with the logo)
        SimpleHeader()

        // Main content column
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Notifications",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))

            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            )

            NotificationItem(
                title = "New Followers",
                subtitle = "When you receive a new follower",
                checked = newFollowers,
                onCheckedChange = { newFollowers = it }
            )

            Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))

            NotificationItem(
                title = "Comments on Reviews",
                subtitle = "When someone comments on your reviews",
                checked = comments,
                onCheckedChange = { comments = it }
            )

            Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))

            NotificationItem(
                title = "Likes",
                subtitle = "When someone likes your reviews or comments",
                checked = likes,
                onCheckedChange = { likes = it }
            )

            Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))

            NotificationItem(
                title = "Recommendations for You",
                subtitle = "Get recommended gastrobares to explore",
                checked = recommendations,
                onCheckedChange = { recommendations = it }
            )

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SimpleHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        LogoApp(modifier = Modifier.size(140.dp))
    }
}

@Composable
private fun NotificationItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    MaterialTheme {
        NotificationScreen()
    }
}