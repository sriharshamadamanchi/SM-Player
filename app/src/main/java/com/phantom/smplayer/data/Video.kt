package com.phantom.smplayer.data

import android.graphics.Bitmap
import android.net.Uri

data class Video(
    val uri: Uri,
    val name: String,
    val duration: Int,
    val thumbnail: Bitmap
)
