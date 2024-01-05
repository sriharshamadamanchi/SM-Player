package com.phantom.smplayer.components.player.video.controls

import androidx.annotation.OptIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.media3.common.TrackSelectionOverride
import androidx.media3.common.Tracks
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.phantom.smplayer.components.Label
import com.phantom.smplayer.components.RadioButton
import com.phantom.smplayer.ui.theme.LocalColor
import java.util.Locale

@OptIn(UnstableApi::class)
@Composable
fun TrackSelection(
    player: ExoPlayer,
    isAudio: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val trackGroups = remember {
            mutableListOf<Tracks.Group>()
        }

        val selectedTrack = remember {
            mutableStateOf("")
        }
        trackGroups.clear()
        for (trackGroup in player.currentTracks.groups) {
            trackGroups.addAll((0 until trackGroup.length)
                .filter {
                    trackGroup.isTrackSupported(it) &&
                            trackGroup.getTrackFormat(it).language != null &&
                            trackGroup.getTrackFormat(it).language != "und" &&
                            trackGroup.getTrackFormat(it).sampleMimeType?.startsWith("audio/") == isAudio
                }
                .map {
                    if (trackGroup.isTrackSelected(it)) {
                        selectedTrack.value = trackGroup.mediaTrackGroup.id + it
                    }
                    trackGroup
                }
            )
        }

        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
            ) {
                Label(
                    title = if (isAudio) "Audio" else "Subtitles",
                    semiBold = true,
                    xl18 = true,
                    center = true,
                    contentColor = LocalColor.Base,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .offset(x = (-10).dp)
                )
            }
            if (trackGroups.isEmpty()) {
                MediaTrack(isTrackSelected = true, title = "Default")
            } else {
                LazyColumn {
                    items(trackGroups) { trackGroup ->
                        repeat(trackGroup.length) { index ->
                            MediaTrack(
                                isTrackSelected = selectedTrack.value == (trackGroup.mediaTrackGroup.id + index),
                                title = "${trackGroup.getTrackFormat(index).label} - " + Locale(
                                    trackGroup.getTrackFormat(index).language ?: "Undefined"
                                ).displayName
                            ) {
                                player.trackSelectionParameters =
                                    player.trackSelectionParameters
                                        .buildUpon()
                                        .setOverrideForType(
                                            TrackSelectionOverride(
                                                trackGroup.mediaTrackGroup,
                                                index
                                            )
                                        )
                                        .build()
                                selectedTrack.value = trackGroup.mediaTrackGroup.id + index
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MediaTrack(
    isTrackSelected: Boolean,
    title: String,
    onSelect: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isTrackSelected
        ){
            onSelect()
        }
        Label(
            title = title,
            maxLines = 2,
            m = true,
            contentColor = LocalColor.Base,
            ellipsis = true,
            modifier = Modifier.fillMaxWidth(fraction = 0.95F)
        )
    }
}