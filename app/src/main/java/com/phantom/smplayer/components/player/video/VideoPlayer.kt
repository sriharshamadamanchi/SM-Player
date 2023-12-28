package com.phantom.smplayer.components.player.video

import android.content.pm.ActivityInfo
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player.REPEAT_MODE_OFF
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.SeekParameters
import androidx.media3.ui.PlayerView
import com.phantom.smplayer.MainActivity
import com.phantom.smplayer.ui.theme.LocalColor
import com.phantom.smplayer.viewmodel.MainViewModel


@OptIn(UnstableApi::class)
@Composable
fun VidePlayer() {

    val mainViewModel: MainViewModel =
        viewModel(viewModelStoreOwner = LocalContext.current as MainActivity)

    val video = mainViewModel.getSelectedVideo()

    val context = LocalContext.current

    val mediaItem = MediaItem.Builder()
        .setUri(video?.uri)
        .build()

    val exoPlayer = remember(context, mediaItem) {
        val loadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(8 * 1024, 64 * 1024, 1024, 1024)
            .setPrioritizeTimeOverSizeThresholds(true)
            .build()

        ExoPlayer.Builder(context)
            .setLoadControl(loadControl)
            .build()
            .apply {
                setSeekParameters(SeekParameters(10000, 10000))
                setMediaItem(mediaItem)
                prepare()
                playWhenReady = false
                repeatMode = REPEAT_MODE_OFF
            }
    }


    DisposableEffect(Unit) {
        val activity = context as MainActivity
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE

        context.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        onDispose {
            exoPlayer.release()
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    val playerView = PlayerView(context).apply {
        useController = false
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        player = exoPlayer
        exoPlayer.play()
    }

    Box {
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .background(LocalColor.Monochrome.Black),
            factory = { playerView }
        )
        MediaControls(
            player = exoPlayer,
            video = video
        )
    }
}