package com.example.myapplication.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.R

@Composable
fun LogoApp(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(R.drawable.logo),
        contentDescription = "Logo",
        modifier = modifier.size(200.dp)
    )
}
