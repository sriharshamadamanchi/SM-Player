package com.phantom.smplayer.components.player.video.controls

import android.content.Context
import android.media.AudioManager
import android.os.Handler
import android.os.Looper
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.zIndex
import androidx.media3.exoplayer.ExoPlayer
import com.phantom.smplayer.R
import com.phantom.smplayer.components.Label
import com.phantom.smplayer.components.SeekBar
import com.phantom.smplayer.components.interaction.NoRippleInteractionSource
import com.phantom.smplayer.components.player.video.VideoActivity
import com.phantom.smplayer.convertMillisecondsToHHmmss
import com.phantom.smplayer.data.Video
import com.phantom.smplayer.ui.theme.LocalColor

enum class Settings {
    NONE,
    AUDIO,
    SUBTITLES
}

@Composable
fun MediaControls(
    player: ExoPlayer,
    video: Video,
    onNavigateBack: () -> Unit
) {

    val activityContext = LocalContext.current as VideoActivity

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
    val addControlDelay = { controlsHandler.postDelayed(controlsListener, 10000) }

    val settings = remember {
        mutableStateOf(Settings.NONE)
    }

    DisposableEffect(player) {
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

        onDispose { handler.removeCallbacks(listener) }
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
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart = { showControls.value = true },
                    onDragCancel = { showControls.value = false },
                    onDragEnd = { showControls.value = false }
                ) { _, dragAmount ->
                    val amount = 500 * dragAmount
                    player.seekTo(player.currentPosition + amount.toLong())
                    sliderPosition.floatValue += amount
                }
            }
    ) {

        AnimatedVisibility(
            visible = showControls.value,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp, vertical = 20.dp)
                .zIndex(1F),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Row {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.5F)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_back),
                        contentDescription = null,
                        tint = LocalColor.Monochrome.White,
                        modifier = Modifier
                            .padding(10.dp)
                            .clickable(
                                interactionSource = NoRippleInteractionSource(), null
                            ) { onNavigateBack() }
                    )
                    Label(
                        title = video.name,
                        white = true,
                        semiBold = true,
                        m = true,
                        maxLines = 2,
                        modifier = Modifier.offset(y = 10.dp)
                    )
                }

                Box(
                    modifier = Modifier.fillMaxWidth(fraction = 0.8F),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Row {
                        Icon(
                            painter = painterResource(id = R.drawable.subtitles),
                            contentDescription = null,
                            tint = LocalColor.Monochrome.White,
                            modifier = Modifier
                                .padding(10.dp)
                                .clickable(
                                    interactionSource = NoRippleInteractionSource(), null
                                ) {
                                    settings.value = Settings.SUBTITLES
                                }
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Icon(
                            painter = painterResource(id = R.drawable.music_note),
                            contentDescription = null,
                            tint = LocalColor.Monochrome.White,
                            modifier = Modifier
                                .padding(10.dp)
                                .clickable(
                                    interactionSource = NoRippleInteractionSource(), null
                                ) {
                                    settings.value = Settings.AUDIO
                                }

                        )
                    }
                }

            }
        }

        AnimatedVisibility(
            visible = (settings.value != Settings.NONE),
            modifier = Modifier
                .width(300.dp)
                .align(Alignment.CenterEnd)
                .zIndex(1F),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            TrackSelection(player = player, isAudio = settings.value == Settings.AUDIO) {
                settings.value = Settings.NONE
            }
        }

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
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )

                Icon(
                    painter = painterResource(id = R.drawable.replay_10),
                    contentDescription = null,
                    tint = LocalColor.Monochrome.White,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .clickable { onDoubleTap(forward = false) }
                )

                Icon(
                    painter = painterResource(id = if (playing.value) R.drawable.pause_circle else R.drawable.play_circle),
                    contentDescription = null,
                    tint = LocalColor.Monochrome.White,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
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
                        .clip(CircleShape)
                        .clickable { onDoubleTap(forward = true) }
                )

                Icon(
                    painter = painterResource(id = R.drawable.skip_next),
                    contentDescription = null,
                    tint = LocalColor.Monochrome.White,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
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
            Column {
                SeekBar(
                    minimumValue = 0F,
                    maximumValue = video.duration.toFloat(),
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-10).dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Label(
                        title = convertMillisecondsToHHmmss(sliderPosition.floatValue.toLong()),
                        white = true,
                        semiBold = true,
                        m = true
                    )

                    Label(
                        title = convertMillisecondsToHHmmss(video.duration.toLong()),
                        white = true,
                        semiBold = true,
                        m = true
                    )
                }
            }
        }
    }
}