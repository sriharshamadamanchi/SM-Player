package com.phantom.smplayer.components.player.video

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import com.phantom.smplayer.data.Video
import com.phantom.smplayer.ui.theme.LocalColor
import com.phantom.smplayer.ui.theme.SMPlayerTheme

class VideoActivity : ComponentActivity() {

    private var exoPlayer: ExoPlayer? = null

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val video = intent.getParcelableExtra<Video>("video")

        val mediaItem = MediaItem.Builder()
            .setUri(video?.uri)
            .build()

        val loadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(8 * 1024, 64 * 1024, 1024, 1024)
            .setPrioritizeTimeOverSizeThresholds(true)
            .build()

        exoPlayer = ExoPlayer.Builder(this)
            .setLoadControl(loadControl)
            .setSeekBackIncrementMs(10000)
            .setSeekForwardIncrementMs(10000)
            .build()
            .apply {
                setMediaItem(mediaItem)
                prepare()
                playWhenReady = false
                repeatMode = Player.REPEAT_MODE_OFF
            }

        setContent {
            SMPlayerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = LocalColor.Base
                ) {
                    VidePlayer(
                        exoPlayer = exoPlayer,
                        video = video
                    ) {
                        finish()
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        exoPlayer?.pause()
    }

    override fun onResume() {
        super.onResume()
        exoPlayer?.play()
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer?.release()
    }
}