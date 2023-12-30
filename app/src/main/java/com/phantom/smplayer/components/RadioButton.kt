package com.phantom.smplayer.components

import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.phantom.smplayer.components.interaction.NoRippleInteractionSource
import com.phantom.smplayer.ui.theme.LocalColor
import androidx.compose.material3.RadioButton as MaterialRadioButton

@Composable
fun RadioButton(
    modifier: Modifier = Modifier,
    disabled: Boolean = false,
    selected: Boolean,
    onClick: () -> Unit = {}
) {
    MaterialRadioButton(
        modifier = modifier,
        enabled = !disabled,
        selected = selected,
        onClick = {
            onClick()
        },
        interactionSource = remember { NoRippleInteractionSource() },
        colors = RadioButtonDefaults.colors(
            selectedColor = LocalColor.Primary.Dark,

            unselectedColor = LocalColor.Monochrome.Black,
        )
    )
}