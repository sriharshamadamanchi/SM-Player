package com.phantom.smplayer.components.player.video.controls

import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

enum class Settings {
    NONE,
    AUDIO,
    SUBTITLES
}

@Composable
fun rememberMediaState(
    initialMediaVolume: Float
): MediaState {
    return remember {
        MediaState(
            initialMediaVolume = initialMediaVolume
        )
    }
}

@Stable
class MediaState(
    initialMediaVolume: Float
) {
    val showMediaControls = mutableStateOf(true)

    val isPlaying = mutableStateOf(true)

    val thumbPosition = mutableFloatStateOf(0F)

    val screenBrightness = mutableFloatStateOf(0F)

    val showScreenBrightness = mutableStateOf(false)

    val mediaVolume = mutableFloatStateOf(initialMediaVolume)

    val showMediaVolume = mutableStateOf(false)

    val isSliding = mutableStateOf(false)

    val settings = mutableStateOf(Settings.NONE)

    private val controlsHandler = Handler(Looper.getMainLooper())

    private val controlsListener = Runnable {
        showMediaControls.value = false
    }

    fun removeControlCallback() {
        controlsHandler.removeCallbacks(controlsListener)
    }

    fun addControlDelay() {
        controlsHandler.postDelayed(controlsListener, 10000)
    }

}