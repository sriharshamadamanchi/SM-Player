package com.phantom.smplayer

import java.text.SimpleDateFormat
import java.util.Locale

fun formatDuration(seconds: Long): String {
    val simpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)

    return simpleDateFormat.format(seconds)
}