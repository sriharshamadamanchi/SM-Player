package com.phantom.smplayer.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.phantom.smplayer.convertMillisecondsToHHmmss
import com.phantom.smplayer.data.Video
import com.phantom.smplayer.ui.theme.LocalColor

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun VideoItem(
    video: Video,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable(enabled = true) {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            bitmap = video.thumbnail.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier
                .width(120.dp)
                .height(80.dp)
                .clip(RoundedCornerShape(5.dp)),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.padding(horizontal = 10.dp)) {
            Label(
                title = video.name,
                contentColor = LocalColor.Monochrome.Light,
                maxLines = 3,
                m = true,
                ellipsis = true
            )
            Spacer(modifier = Modifier.height(5.dp))
            Label(
                title = convertMillisecondsToHHmmss(video.duration.toLong()),
                contentColor = LocalColor.Monochrome.Medium,
                s = true
            )
        }
    }
}