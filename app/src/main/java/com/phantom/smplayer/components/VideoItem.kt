package com.phantom.smplayer.components

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Size
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.phantom.smplayer.ui.theme.LocalColor

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun VideoItem(
    videoUri: Uri,
    videoName: String,
    duration: Int,
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

        val thumbnail: Bitmap =
            LocalContext.current.contentResolver.loadThumbnail(
                videoUri, Size(480, 480), null
            )

        Image(
            bitmap = thumbnail.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier
                .width(120.dp)
                .height(80.dp)
                .clip(RoundedCornerShape(5.dp)),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.padding(horizontal = 10.dp)) {
            Label(
                title = videoName,
                contentColor = LocalColor.Monochrome.Light,
                maxLines = 3,
                m = true,
                ellipsis = true
            )
//            Label(
//                title = formatDuration(duration.toLong()),
//                contentColor = LocalColor.Monochrome.Medium,
//                s = true
//            )
        }
    }
}