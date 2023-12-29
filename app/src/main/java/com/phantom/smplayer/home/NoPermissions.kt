package com.phantom.smplayer.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.phantom.smplayer.components.Label

@Composable
fun NoPermissions(
    openSettings: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Label(
                title = "App requires access to photos and videos on this device",
                white = true,
                semiBold = true,
                modifier = Modifier.fillMaxWidth(fraction = 0.8F),
                maxLines = 2,
                center = true
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(onClick = {
                openSettings()
            }) {
                Label(title = "Open Settings", white = true, semiBold = true)
            }
        }
    }
}