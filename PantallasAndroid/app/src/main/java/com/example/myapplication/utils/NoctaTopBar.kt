package com.example.myapplication.utils

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.noctaLogoFont

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoctaTopBar(){
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Nocta",
                fontWeight = FontWeight.Bold,
                fontFamily = noctaLogoFont,
                fontSize = 35.sp,
                style = MaterialTheme.typography.displayLarge
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        )
    )
}

@Composable
@Preview(showBackground = true)
fun NoctaTopBarPreview(){
    NoctaTopBar()
}