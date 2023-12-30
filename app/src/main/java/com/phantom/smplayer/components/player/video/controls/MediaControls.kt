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

@Composable
fun MediaControls(
    player: ExoPlayer,
    video: Video,
    onNavigateBack: () -> Unit
) {

    val activityContext = LocalContext.current as VideoActivity

    val audioManager = remember {
        activityContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }
    val mediaState = rememberMediaState(
        initialMediaVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()
    )

    DisposableEffect(player) {
        mediaState.addControlDelay()

        val handler = Handler(Looper.getMainLooper())

        val listener = object : Runnable {
            override fun run() {
                if (!mediaState.isSliding.value) {
                    val currentPosition = player.currentPosition.toFloat()
                    mediaState.thumbPosition.floatValue = currentPosition
                }
                handler.postDelayed(this, 1000)
            }
        }

        handler.postDelayed(listener, 1000)

        onDispose { handler.removeCallbacks(listener) }
    }

    fun onTap() {
        mediaState.removeControlCallback()
        if (!mediaState.showMediaControls.value) {
            mediaState.addControlDelay()
        }
        mediaState.showMediaControls.value = !mediaState.showMediaControls.value
    }

    fun onDoubleTap(forward: Boolean) {
        if (forward) {
            player.seekForward()
        } else {
            player.seekBack()
        }
        mediaState.thumbPosition.floatValue = player.currentPosition.toFloat()
        mediaState.removeControlCallback()
        mediaState.addControlDelay()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = { onTap() })
            }
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    val amount = 100 * dragAmount
                    player.seekTo(player.currentPosition + amount.toLong())
                    mediaState.thumbPosition.floatValue += amount
                }
            }
    ) {

        AnimatedVisibility(
            visible = mediaState.showMediaControls.value,
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
                                    mediaState.settings.value = Settings.SUBTITLES
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
                                    mediaState.settings.value = Settings.AUDIO
                                }
                        )
                    }
                }

            }
        }

        AnimatedVisibility(
            visible = (mediaState.settings.value != Settings.NONE),
            modifier = Modifier
                .width(300.dp)
                .align(Alignment.CenterEnd)
                .zIndex(1F),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            TrackSelection(player = player, isAudio = mediaState.settings.value == Settings.AUDIO) {
                mediaState.settings.value = Settings.NONE
            }
        }

        // Left: Volume Gesture Listener
        GestureView(
            flagState = mediaState.showMediaVolume,
            previousState = mediaState.mediaVolume,
            alignment = Alignment.CenterStart,
            onDrag = {
                audioManager.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    16 - mediaState.mediaVolume.floatValue.toInt(),
                    0
                )
            },
            onTap = { onTap() },
            onDoubleTap = { onDoubleTap(forward = false) }
        ) {

            // Brightness Slider
            if (mediaState.showScreenBrightness.value) {
                SwipeControl(
                    state = mediaState.screenBrightness,
                    isBrightnessControl = true
                )
            }
        }

        // Right: Brightness Listener
        GestureView(
            flagState = mediaState.showScreenBrightness,
            previousState = mediaState.screenBrightness,
            alignment = Alignment.CenterEnd,
            onDrag = {
                val attributes = activityContext.window.attributes
                attributes.screenBrightness = (16F - mediaState.screenBrightness.floatValue) / 16F

                activityContext.window.attributes = attributes
            },
            onTap = { onTap() },
            onDoubleTap = { onDoubleTap(forward = true) }
        ) {
            // Volume Slider
            if (mediaState.showMediaVolume.value) {
                SwipeControl(
                    state = mediaState.mediaVolume,
                    isBrightnessControl = false
                )
            }
        }

        AnimatedVisibility(
            visible = mediaState.showMediaControls.value,
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
                    painter = painterResource(id = if (mediaState.isPlaying.value) R.drawable.pause_circle else R.drawable.play_circle),
                    contentDescription = null,
                    tint = LocalColor.Monochrome.White,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .clickable {
                            if (mediaState.isPlaying.value) {
                                player.pause()
                            } else {
                                player.play()
                            }

                            mediaState.isPlaying.value = !mediaState.isPlaying.value
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
            visible = mediaState.showMediaControls.value,
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
                    sliderValue = mediaState.thumbPosition.floatValue,
                    onSlidingComplete = {
                        mediaState.isSliding.value = false
                        mediaState.addControlDelay()
                        player.seekTo(mediaState.thumbPosition.floatValue.toLong())
                    },
                    onValueChange = {
                        mediaState.isSliding.value = true
                        mediaState.removeControlCallback()
                        mediaState.thumbPosition.floatValue = it
                    }
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-10).dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Label(
                        title = convertMillisecondsToHHmmss(mediaState.thumbPosition.floatValue.toLong()),
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