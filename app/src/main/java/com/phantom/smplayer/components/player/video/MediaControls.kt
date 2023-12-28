package com.phantom.smplayer.components.player.video

import android.content.Context
import android.media.AudioManager
import android.os.Handler
import android.os.Looper
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.media3.exoplayer.ExoPlayer
import com.phantom.smplayer.MainActivity
import com.phantom.smplayer.R
import com.phantom.smplayer.components.SeekBar
import com.phantom.smplayer.data.Video
import com.phantom.smplayer.ui.theme.LocalColor

@Composable
fun MediaControls(
    player: ExoPlayer,
    video: Video?
) {

    val activityContext = LocalContext.current as MainActivity

    val showControls = remember {
        mutableStateOf(true)
    }

    val playing = remember {
        mutableStateOf(true)
    }

    val sliderPosition = remember {
        mutableFloatStateOf(0F)
    }

    val brightness = remember {
        mutableFloatStateOf(0F)
    }

    val showBrightness = remember {
        mutableStateOf(false)
    }

    val controlsHandler = remember { Handler(Looper.getMainLooper()) }
    val controlsListener = remember {
        Runnable {
            showControls.value = false
        }
    }

    val audioManager = remember {
        activityContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    val volume = remember {
        mutableFloatStateOf(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat())
    }

    val showVolume = remember {
        mutableStateOf(false)
    }

    val sliding = remember {
        mutableStateOf(false)
    }

    val removeControlCallback = { controlsHandler.removeCallbacks(controlsListener) }
    val addControlDelay = { controlsHandler.postDelayed(controlsListener, 5000) }

    LaunchedEffect(player) {
        addControlDelay()

        val handler = Handler(Looper.getMainLooper())

        val listener = object : Runnable {
            override fun run() {
                if (!sliding.value) {
                    val currentPosition = player.currentPosition.toFloat()
                    sliderPosition.floatValue = currentPosition
                }
                handler.postDelayed(this, 1000)
            }
        }

        handler.postDelayed(listener, 1000)
    }

    fun onTap() {
        removeControlCallback()
        if (!showControls.value) {
            addControlDelay()
        }
        showControls.value = !showControls.value
    }

    fun onDoubleTap(forward: Boolean) {
        if (forward) {
            player.seekForward()
        } else {
            player.seekBack()
        }
        sliderPosition.floatValue = player.currentPosition.toFloat()
        removeControlCallback()
        addControlDelay()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = { onTap() })
            }
    ) {

        // Left: Volume Gesture Listener
        GestureView(
            flagState = showVolume,
            previousState = volume,
            alignment = Alignment.CenterStart,
            onDrag = {
                audioManager.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    16 - volume.floatValue.toInt(),
                    0
                )
            },
            onTap = { onTap() },
            onDoubleTap = { onDoubleTap(forward = false) }
        ) {

            // Brightness Slider
            if (showBrightness.value) {
                SwipeControl(
                    state = brightness,
                    isBrightnessControl = true
                )
            }
        }

        // Right: Brightness Listener
        GestureView(
            flagState = showBrightness,
            previousState = brightness,
            alignment = Alignment.CenterEnd,
            onDrag = {
                val attributes = activityContext.window.attributes
                attributes.screenBrightness = (16F - brightness.floatValue) / 16F

                activityContext.window.attributes = attributes
            },
            onTap = { onTap() },
            onDoubleTap = { onDoubleTap(forward = true) }
        ) {
            // Volume Slider
            if (showVolume.value) {
                SwipeControl(
                    state = volume,
                    isBrightnessControl = false
                )
            }
        }

        AnimatedVisibility(
            visible = showControls.value,
            modifier = Modifier
                .fillMaxHeight()
                .width(350.dp)
                .align(Alignment.Center),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.skip_previous),
                    contentDescription = null,
                    tint = LocalColor.Monochrome.White,
                    modifier = Modifier.size(48.dp)
                )

                Icon(
                    painter = painterResource(id = R.drawable.replay_10),
                    contentDescription = null,
                    tint = LocalColor.Monochrome.White,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { onDoubleTap(forward = false) }
                )

                Icon(
                    painter = painterResource(id = if (playing.value) R.drawable.pause_circle else R.drawable.play_circle),
                    contentDescription = null,
                    tint = LocalColor.Monochrome.White,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable {
                            if (playing.value) {
                                player.pause()
                            } else {
                                player.play()
                            }

                            playing.value = !playing.value
                        }
                )

                Icon(
                    painter = painterResource(id = R.drawable.forward_10),
                    contentDescription = null,
                    tint = LocalColor.Monochrome.White,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { onDoubleTap(forward = true) }
                )

                Icon(
                    painter = painterResource(id = R.drawable.skip_next),
                    contentDescription = null,
                    tint = LocalColor.Monochrome.White,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        AnimatedVisibility(
            visible = showControls.value,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(fraction = 0.95F)
                .padding(bottom = 20.dp),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            SeekBar(
                minimumValue = 0F,
                maximumValue = video?.duration?.toFloat() ?: 100F,
                sliderValue = sliderPosition.floatValue,
                onSlidingComplete = {
                    sliding.value = false
                    addControlDelay()
                    player.seekTo(sliderPosition.floatValue.toLong())
                },
                onValueChange = {
                    sliding.value = true
                    removeControlCallback()
                    sliderPosition.floatValue = it
                }
            )
        }

    }
}


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