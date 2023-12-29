package com.phantom.smplayer.components.player.video

import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.phantom.smplayer.data.Video
import com.phantom.smplayer.ui.theme.LocalColor


@OptIn(UnstableApi::class)
@Composable
fun VidePlayer(
    exoPlayer: ExoPlayer?,
    video: Video?,
    onNavigateBack: () -> Unit
) {

    if (exoPlayer != null) {
        Box {
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LocalColor.Monochrome.Black),
                factory = {
                    PlayerView(it).apply {
                        useController = false
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )

                        player = exoPlayer
                        exoPlayer.play()
                    }
                }
            )

            if (video != null) {
                MediaControls(
                    player = exoPlayer,
                    video = video,
                    onNavigateBack = onNavigateBack
                )
            }
        }
    }
}