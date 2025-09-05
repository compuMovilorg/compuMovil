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
import com.example.myapplication.utils.NotificationItem

@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    viewModel: NotificationViewModel
) {
    val state by viewModel.uiState.collectAsState()

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
            checked = state.newFollowers,
            onCheckedChange = { viewModel.setNewFollowers(it) }
        )

        Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))

        NotificationItem(
            title = "Comments on Reviews",
            subtitle = "When someone comments on your reviews",
            checked = state.comments,
            onCheckedChange = { viewModel.setComments(it) }
        )

        Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))

        NotificationItem(
            title = "Likes",
            subtitle = "When someone likes your reviews or comments",
            checked = state.likes,
            onCheckedChange = { viewModel.setLikes(it) }
        )

        Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))

        NotificationItem(
            title = "Recommendations for You",
            subtitle = "Get recommended gastrobares to explore",
            checked = state.recommendations,
            onCheckedChange = { viewModel.setRecommendations(it) }
        )

        Spacer(Modifier.height(16.dp))
    }
}



//@Preview(showBackground = true)
//@Composable
//private fun NotificationScreenPreview() {
//    MaterialTheme {
//        NotificationScreen(viewModel = NotificationViewModel())
//    }
//}
