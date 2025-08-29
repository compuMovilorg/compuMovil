package com.example.myapplication.ui.notification

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState

@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    viewModel: NotificationViewModel = viewModel()
) {
    val newFollowers by viewModel.newFollowers.collectAsState()
    val comments by viewModel.comments.collectAsState()
    val likes by viewModel.likes.collectAsState()
    val recommendations by viewModel.recommendations.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 100.dp)
    ) {
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Notificaciones",
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
            onCheckedChange = { viewModel.setNewFollowers(it) }
        )

        Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))

        NotificationItem(
            title = "Comments on Reviews",
            subtitle = "When someone comments on your reviews",
            checked = comments,
            onCheckedChange = { viewModel.setComments(it) }
        )

        Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))

        NotificationItem(
            title = "Likes",
            subtitle = "When someone likes your reviews or comments",
            checked = likes,
            onCheckedChange = { viewModel.setLikes(it) }
        )

        Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))

        NotificationItem(
            title = "Recommendations for You",
            subtitle = "Get recommended gastrobares to explore",
            checked = recommendations,
            onCheckedChange = { viewModel.setRecommendations(it) }
        )

        Spacer(Modifier.height(16.dp))
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

//@Preview(showBackground = true)
//@Composable
//private fun NotificationScreenPreview() {
//    MaterialTheme {
//        NotificationScreen(viewModel = NotificationViewModel())
//    }
//}
