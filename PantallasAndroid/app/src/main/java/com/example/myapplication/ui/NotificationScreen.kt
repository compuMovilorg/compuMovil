package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NotificationSettingsScreen(
    // estados iniciales (puedes conectarlos a ViewModel si quieres)
    newFollowers: Boolean = true,
    commentsOnReviews: Boolean = true,
    likes: Boolean = true,
    recommendations: Boolean = false,
    onBack: (() -> Unit)? = null,
    onChanged: (NotificationPrefs) -> Unit = {}
) {
    var followers by rememberSaveableBool(newFollowers)
    var comments  by rememberSaveableBool(commentsOnReviews)
    var likesSt   by rememberSaveableBool(likes)
    var recs      by rememberSaveableBool(recommendations)

    fun emit() = onChanged(
        NotificationPrefs(
            newFollowers = followers,
            commentsOnReviews = comments,
            likes = likesSt,
            recommendations = recs
        )
    )

    Scaffold(
        topBar = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Marca "Nocta"
                Text(
                    text = "Nocta",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Notification",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        bottomBar = {
            // Placeholder de barra inferior (si tienes tu propia BottomBar, reemplázala aquí)
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            SettingRow(
                title = "New Followers",
                subtitle = "When you receive a new follower",
                checked = followers,
                onCheckedChange = {
                    followers = it; emit()
                }
            )
            Divider()
            SettingRow(
                title = "Comments on Reviews",
                subtitle = "When someone comments on your reviews",
                checked = comments,
                onCheckedChange = {
                    comments = it; emit()
                }
            )
            Divider()
            SettingRow(
                title = "Likes",
                subtitle = "When someone likes your reviews or comments",
                checked = likesSt,
                onCheckedChange = {
                    likesSt = it; emit()
                }
            )
            Divider()
            SettingRow(
                title = "Recommendations for You",
                subtitle = "Get recommended gastrobares to explore",
                checked = recs,
                onCheckedChange = {
                    recs = it; emit()
                }
            )
            Divider()
        }
    }
}

@Composable
private fun SettingRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 64.dp)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

data class NotificationPrefs(
    val newFollowers: Boolean,
    val commentsOnReviews: Boolean,
    val likes: Boolean,
    val recommendations: Boolean
)

// Helper para rememberSaveable Boolean
@Composable
private fun rememberSaveableBool(initial: Boolean): MutableState<Boolean> =
    rememberSaveable { mutableStateOf(initial) }

@Preview(showBackground = true)
@Composable
private fun NotificationSettingsPreview() {
    NotificationSettingsScreen()
}