package com.phantom.smplayer.components.player.video.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.phantom.smplayer.R
import com.phantom.smplayer.ui.theme.LocalColor

@Composable
fun BoxScope.SwipeControl(
    state: MutableFloatState,
    isBrightnessControl: Boolean
) {
    Box(
        modifier = Modifier
            .width(45.dp)
            .height(200.dp)
            .clip(RoundedCornerShape(40.dp))
            .background(LocalColor.Monochrome.Dark.copy(alpha = 0.5F))
            .align(Alignment.Center)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 1F - state.floatValue / 16F)
                .background(
                    LocalColor.Monochrome.White
                )
                .align(Alignment.BottomCenter)
        )

        val normal = remember {
            if (isBrightnessControl) {
                R.drawable.light_mode
            } else {
                R.drawable.music_note
            }
        }

        val off = remember {
            if (isBrightnessControl) {
                R.drawable.backlight_low
            } else {
                R.drawable.music_off
            }
        }

        val low = (16F - state.floatValue) <= 3F
        Icon(
            painter = painterResource(id = if (low) off else normal),
            contentDescription = null,
            tint = if (low) LocalColor.Monochrome.White else LocalColor.Monochrome.Black,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp)

        )
    }
}