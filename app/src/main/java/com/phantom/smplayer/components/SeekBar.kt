package com.phantom.smplayer.components

import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.phantom.smplayer.ui.theme.LocalColor

@Composable
fun SeekBar(
    modifier: Modifier = Modifier,
    sliderValue: Float,
    enabled: Boolean = true,
    minimumValue: Float = 1f,
    maximumValue: Float = 100f,
    step: Int = 0,
    onValueChange: (value: Float) -> Unit,
    onSlidingComplete: () -> Unit = {}
) {

    val thumbColor = LocalColor.Monochrome.White

    val inActiveTrackColor = LocalColor.Monochrome.Regular

    val activeTrackColor = LocalColor.Monochrome.White

    Slider(
        value = sliderValue,
        modifier = Modifier
            .then(modifier),
        enabled = enabled,
        onValueChange = onValueChange,
        onValueChangeFinished = onSlidingComplete,
        valueRange = minimumValue..maximumValue,
        steps = step,
        colors = SliderDefaults.colors(
            thumbColor = thumbColor,
            activeTrackColor = activeTrackColor,
            inactiveTrackColor = inActiveTrackColor
        )
    )
}