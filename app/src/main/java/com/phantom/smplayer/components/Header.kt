package com.phantom.smplayer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.phantom.smplayer.R
import com.phantom.smplayer.components.interaction.NoRippleInteractionSource
import com.phantom.smplayer.ui.theme.LocalColor

@Composable
fun Header(
    headerTitle: String = "",
    containerColor: Color = LocalColor.Base,
    contentColor: Color = LocalColor.Base,
    containerHeight: Dp = 60.dp,
    showBackButton: Boolean = true,
    onBackPress: () -> Unit = {}
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(containerHeight)
        .graphicsLayer {
            shadowElevation = 5F
        }
        .background(containerColor)
    ) {

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(60.dp)
                .clickable(
                    interactionSource = NoRippleInteractionSource(),
                    indication = null,
                    enabled = true
                ) { onBackPress() },
            contentAlignment = Alignment.Center
        ) {
            if (showBackButton) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_back),
                    contentDescription = "arrow-back",
                    tint = contentColor,
                    modifier = Modifier
                        .size(24.dp)
                )
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction = 0.6F),
                contentAlignment = Alignment.Center
            ) {
                Label(
                    title = headerTitle,
                    medium = true,
                    xl18 = true,
                    contentColor = contentColor,
                    ellipsis = true
                )
            }
        }
    }
}