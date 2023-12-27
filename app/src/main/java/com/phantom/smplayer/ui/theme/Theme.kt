package com.phantom.smplayer.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView

@Composable
fun SMPlayerTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color(0XFF010614).toArgb()
        }
    }

    val customColorScheme = MaterialTheme.colorScheme.copy(
        primary = LocalColor.Primary.Dark
    )

    MaterialTheme(
        content = content,
        colorScheme = customColorScheme
    )
}