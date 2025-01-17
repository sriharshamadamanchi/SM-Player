package com.phantom.smplayer.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.phantom.smplayer.ui.theme.LocalColor

@OptIn(ExperimentalMaterial3Api::class)
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

    val interactionSource = remember { MutableInteractionSource() }
    val sliderColors = SliderDefaults.colors(
        thumbColor = LocalColor.Monochrome.White,
        activeTrackColor = LocalColor.Monochrome.White,
        inactiveTrackColor = LocalColor.Monochrome.Regular
    )

    Slider(
        value = sliderValue,
        modifier = Modifier
            .then(modifier),
        enabled = enabled,
        onValueChange = onValueChange,
        onValueChangeFinished = onSlidingComplete,
        valueRange = minimumValue..maximumValue,
        steps = step,
        colors = sliderColors,
        thumb = { _ ->
            SliderDefaults.Thumb(
                thumbSize = DpSize(20.dp, 20.dp),
                colors = sliderColors,
                interactionSource = interactionSource
            )
        },
        track = { sliderState ->
            SliderDefaults.Track(
                modifier = Modifier
                    .height(5.dp),
                colors = sliderColors,
                sliderState = sliderState,
                thumbTrackGapSize = 0.dp
            )
        }
    )
}