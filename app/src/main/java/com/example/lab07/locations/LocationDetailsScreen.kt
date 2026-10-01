package com.example.lab07.locations

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun LocationDetailsScreen (
    locationId: Int,
    onNavigateBack: () -> Unit
) {
    Text(
        text = "Location Details Screen: $locationId"
    )
}