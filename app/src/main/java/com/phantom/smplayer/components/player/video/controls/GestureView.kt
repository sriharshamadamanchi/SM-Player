package com.phantom.smplayer.components.player.video.controls

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun BoxScope.GestureView(
    flagState: MutableState<Boolean>,
    previousState: MutableFloatState,
    alignment: Alignment,
    onDrag: () -> Unit,
    onTap: () -> Unit,
    onDoubleTap: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth(fraction = 0.3F)
        .align(alignment)
        .pointerInput(Unit) {
            detectTapGestures(
                onTap = { onTap() },
                onDoubleTap = { onDoubleTap() })
        }
        .pointerInput(Unit) {
            detectVerticalDragGestures(
                onDragStart = { flagState.value = true },
                onDragEnd = { flagState.value = false },
                onDragCancel = { flagState.value = false }
            ) { _, dragAmount ->
                previousState.floatValue += dragAmount / 32F
                previousState.floatValue = previousState.floatValue.coerceIn(0F, 16F)

                onDrag()
            }
        },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}