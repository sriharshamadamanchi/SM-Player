package com.phantom.smplayer.components.player.video.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.phantom.smplayer.R
import com.phantom.smplayer.components.Label

val settings = listOf(SettingsState.AUDIO, SettingsState.SUBTITLES)

@Composable
fun BoxScope.Settings(
    player: ExoPlayer,
    onClose: () -> Unit
) {
    val navController = rememberNavController()

    val selectedRoute = remember {
        mutableStateOf(SettingsState.AUDIO.name)
    }

    LaunchedEffect(Unit) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            selectedRoute.value = destination.route ?: SettingsState.AUDIO.name
        }
    }

    Box(
        modifier = Modifier
            .size(500.dp)
            .background(Color.Transparent)
            .align(Alignment.Center)
            .pointerInput(Unit) { detectTapGestures { } }
            .pointerInput(Unit) { detectDragGestures { _, _ -> } },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(300.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
            ) {
                LazyColumn(
                    modifier = Modifier.padding(10.dp)
                ) {
                    items(settings) {
                        Box(
                            modifier = Modifier
                                .width(120.dp)
                                .height(45.dp)
                                .padding(5.dp)
                                .clip(
                                    RoundedCornerShape(10.dp)
                                )
                                .shadow(1.dp)
                                .background(
                                    brush = Brush.linearGradient(
                                        if (selectedRoute.value == it.name)
                                            listOf(
                                                Color(0xFF11998E),
                                                Color(0xFF38EF7D),
                                            )
                                        else
                                            listOf(
                                                Color(0xFF959595),
                                                Color(0xFF5B5B5B),
                                            )
                                    )
                                )
                                .clickable {
                                    navController.navigate(it.name)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Label(title = it.name, s = true, semiBold = true, white = true)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.width(10.dp))

            Box(modifier = Modifier) {

                Icon(
                    painter = painterResource(id = R.drawable.close), contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .zIndex(10F)
                        .clickable {
                            onClose()
                        }
                )

                NavHost(
                    navController = navController,
                    startDestination = SettingsState.AUDIO.name,
                    modifier = Modifier
                        .width(300.dp)
                        .height(300.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White)
                ) {
                    composable(SettingsState.AUDIO.name) {
                        TrackSelection(
                            player = player,
                            isAudio = true
                        )
                    }

                    composable(SettingsState.SUBTITLES.name) {
                        TrackSelection(
                            player = player,
                            isAudio = false
                        )
                    }
                }
            }
        }
    }
}